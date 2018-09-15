package org.ruoxue.backend.controller;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.config.wxpay.WePayConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pay/wepay/")
public class WepayController {

    @RequestMapping("start")
    public void start(@RequestParam String orderID, HttpServletResponse response) {
        if (!Constant.WxPayConfig.WX_CONFIGURED) {
            response.setStatus(503);
            return;
        }

        try {
            WePayConfig config = new WePayConfig();
            WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, true);

            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "商品名称  查出来");
            data.put("out_trade_no", "2016090910595900000012");
            data.put("device_info", "");
            data.put("fee_type", "CNY");
            data.put("total_fee", "1.00");
            data.put("spbill_create_ip", "123.12.12.123");
            data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            data.put("product_id", "12");

            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);

        }
        catch (Exception e) {

        }

    }

    @RequestMapping("notify")
    public void notify(@RequestParam String notifyData) {
        try {
            WePayConfig config = new WePayConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map

            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            }
            else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("query")
    public void query(@RequestParam("tradeno") String tradeNo) {
        try {
            WePayConfig config = new WePayConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", tradeNo);

            Map<String, String> resp = wxpay.orderQuery(data);
            System.out.println(resp);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
