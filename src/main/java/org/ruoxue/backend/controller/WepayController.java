package org.ruoxue.backend.controller;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.config.wxpay.WePayConfig;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TExchangeMapper;
import org.ruoxue.backend.service.IAlipayService;
import org.ruoxue.backend.service.ITLogsService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pay/wepay/")
public class WepayController {

    @Resource
    private IAlipayService alipayService;

    @Resource
    ITLogsService logsService;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TExchangeMapper exchangeMapper;

    @RequestMapping("start")
    public Object start(@RequestParam Integer amount, HttpServletRequest request, HttpServletResponse response) {
        if (!Constant.WxPayConfig.WX_CONFIGURED) {
            return ResultUtil.error(-1, "服务器端未配置微信支付");
        }

        try {
            TExchange exchange = new TExchange();
            exchange.setUid(XunBinKit.getUid());
            exchange.setTm(new Date());
            exchange.setPaymethod(Constant.PaymentMethod.ONLINE_WECHAT);
            exchange.setCid(-1);
            exchange.setState(Constant.ExchangeStatus.UNPAIED);
            Integer orderID = exchangeMapper.insertReturnsID(exchange);

            WePayConfig config = new WePayConfig();
            WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, true);

            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "增薪宝-在线充值" + amount + "元");
            data.put("out_trade_no", orderID.toString());
            data.put("device_info", "");
            data.put("fee_type", "CNY");
            data.put("total_fee", "1.00");
            data.put("spbill_create_ip", request.getRemoteAddr());
            data.put("notify_url", Constant.WxPayConfig.NOTIFY_URL);
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            data.put("product_id", "0"+ amount);

            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);

            /// 将codeurl生成图片[前端完成]
            if (wxpay.isPayResultNotifySignatureValid(resp)) {
                // 签名正确进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                return ResultUtil.success(resp);
            }
            else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                return ResultUtil.error(-1, "签名校验失败");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(-2, e.getMessage());
        }

    }

    public String generateWxNotifyResult(boolean success) {
        return "<xml>\n" +
                "\n" +
                "  <return_code><![CDATA["+ (success ? "SUCCESS": "FAIL") +"]]></return_code>\n" +
                "  <return_msg><![CDATA[" + (success ? "OK" : "FAIL") + "]]></return_msg>\n" +
                "</xml>";
    }

    public boolean processWxLogic(Map<String, String> notifyMap) {
        try {
            WePayConfig config = new WePayConfig();
            WXPay wxpay = new WXPay(config);

            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                Integer orderid = Integer.valueOf(notifyMap.get("out_trade_no"));

                // 确认是否有该订单, 该订单是否属于该用户
                TExchange exchange = exchangeMapper.getEntityByID(orderid);
                if (ToolUtil.isEmpty(exchange) || exchange.getUid() != XunBinKit.getUid()) {
                    /// TODO: 添加log  应该不会出现这种情况
                    return false;
                }

                if (!notifyMap.get("result_code").equals("SUCCESS")) {
                    return false;
                }

                // 记录微信订单号
                exchangeMapper.updateRunningByID(orderid, notifyMap.get("transaction_id"));
                // 更新付款状态
                exchangeMapper.updateStateByID(orderid, Constant.PaymentStatus.PAIED);
                // 更新余额
                //   订单总金额，单位为分,需要除以100
                customerMapper.updateBalanceRelative(Double.parseDouble(notifyMap.get("total_fee")) / 100, XunBinKit.getUid());

                return true;
            }
            else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                logsService.actionLogNow(Constant.LogUsers.SYSTEM,
                        "非法的微信API接口请求: " + notifyMap.toString(),
                        Constant.LogClass.DANGEROUS);
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            logsService.actionLogNow(Constant.LogUsers.SYSTEM,
                    "处理微信订单时发生错误: " + notifyMap.toString(),
                    Constant.LogClass.IMPORTANT);
            return false;
        }
    }

    @RequestMapping("notify")
    public String notify(@RequestParam String notifyData) {
        try {
            WePayConfig config = new WePayConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map
            return generateWxNotifyResult(processWxLogic(notifyMap));
        }
        catch (Exception e) {
            return generateWxNotifyResult(false);
        }

    }

    @RequestMapping("get-result")
    public Object getResult() {
        if (null == XunBinKit.getUid())
            return ResultUtil.error(-1, "用户未登录");
        TExchange exchange = exchangeMapper.getLastTransaction();
        if (ToolUtil.isNotEmpty(exchange))
            return ResultUtil.error(-2, "No such transaction");
        if (exchange.getPaymethod() != Constant.PaymentMethod.ONLINE_WECHAT) {
            return ResultUtil.error(-13, "无法查询订单状态：该订单不是微信支付订单，请到交易列表手动查询");
        }
        return ResultUtil.success(exchange.getState() == Constant.PaymentStatus.PAIED);
    }

    @RequestMapping("query")
    public Object query(@RequestParam("eid") Integer orderid) {
        try {
            WePayConfig config = new WePayConfig();
            WXPay wxpay = new WXPay(config);

            // 确认是否有该订单, 该订单是否属于该用户
            TExchange exchange = exchangeMapper.getEntityByID(orderid);
            if (ToolUtil.isEmpty(exchange) || exchange.getUid() != XunBinKit.getUid()) {
                /// TODO: 添加log  应该不会出现这种情况
                return ResultUtil.error(-1, "该订单不存在");
            }

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", orderid.toString());

            Map<String, String> resp = wxpay.orderQuery(data);

            if (!resp.get("return_code").equals("SUCCESS")) {
                return ResultUtil.error(-2, "通讯错误");
            }

            if (!resp.get("trade_state").equals("SUCCESS")) {
                return ResultUtil.error(-3, "交易失败");
            }

            return ResultUtil.success(processWxLogic(resp));
        }
        catch (Exception e) {
            e.printStackTrace();
            logsService.actionLogNow(Constant.LogUsers.SYSTEM,
                    "处理微信订单时发生错误: orderid=" + orderid,
                    Constant.LogClass.IMPORTANT);
            return ResultUtil.error(-4, e.getMessage());
        }
    }

}
