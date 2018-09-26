package org.ruoxue.backend.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TExchangeMapper;
import org.ruoxue.backend.mapper.TOrderMapper;
import org.ruoxue.backend.service.IAlipayService;
import org.ruoxue.backend.service.ITLogsService;
import org.ruoxue.backend.util.LoopAction;
import org.ruoxue.backend.util.PaymentResultUtil;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.ruoxue.backend.common.constant.Constant.AlipayConfig.*;

@RestController
@RequestMapping("/api/pay/alipay/")
public class AlipayController {

    @Resource
    ITLogsService logsService;

    @Resource
    private IAlipayService alipayService;

    @Resource
    private TExchangeMapper exchangeMapper;

    @Resource
    private TCustomerMapper customerMapper;

//    添加事务管理(保证五性)
    @Transactional
    @RequestMapping("start")
    public void startPayment(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                             @RequestParam("dst") Integer dst,
                             @RequestParam(required = false) Integer itemid, @RequestParam(required = false) String name, @RequestParam(required = false) Double amount) {
//        调用service层
        alipayService.startPayment(httpRequest, httpResponse, amount, dst);

    }

    /// 这个函数是回调  但是不一定确实成功了，只是给用户显示的界面
    /// 结果以下面那个notify为准
    @RequestMapping("finish")
    public void finishPaymant(@RequestParam("out_trade_no") String out_trade_no,
                                       @RequestParam("total_amount") Double total_amount,
                                       @RequestParam("sign") String sign,
                                       @RequestParam("trade_no") String trade_no,
                                       @RequestParam("auth_app_id") String auth_app_id,
                                       @RequestParam("app_id") String app_id,
                                       @RequestParam("seller_id") String seller_id,
                                       @RequestParam("timestamp") String timestamp,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // 避免其他人来自非本站的请作为回调
            if (!seller_id.equals(Constant.AlipayConfig.PROVIDER_ID)) {
                response.setStatus(403);
                return;
            }

            alipayService.finishPaymant(Integer.parseInt(out_trade_no), request, response);

//        将参数显示
            response.getWriter().println(
                PaymentResultUtil.paymentResult(out_trade_no, total_amount.toString(), trade_no, timestamp, "支付宝")
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("notify")
    public void notifyQuery(@RequestParam HashMap<String, String> params, HttpServletResponse response) {
        Map<String, String> paramsMap = params;
        System.err.println("[AlipayController] 接收到支付宝的异步通知");
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, Constant.AlipayConfig.ALIPAY_PUBLIC_KEY, CHARSET, Constant.AlipayConfig.SIGN_TYPE); //调用SDK验证签名
            if(signVerified){
                System.err.println("[AlipayController] 延签成功");
                // 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
                // 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
                Integer orderid = Integer.parseInt(params.get("out_trade_no"));

                System.err.println("[AlipayController] 参数内容： " + params.toString());
//                调用service
                alipayService.notifyQuery(orderid, response, params);
                response.getWriter().println("success");
            }else{
                // 验签失败则记录异常日志，并在response中返回failure.
                response.getWriter().println("failure");
                System.err.println("[AlipayController] 延签失败");

                response.getOutputStream().println("非法访问者你好，你的APP ID我们已经记下，我们将保存这个请求并向支付宝举报。");
                logsService.actionLogNow(Constant.LogUsers.SYSTEM,
                        "非法的支付宝API接口请求: " + params.toString(),
                        Constant.LogClass.DANGEROUS);
            }
        }
        catch (Exception e) {
            System.err.println("[AlipayController] 发生异常");
            e.printStackTrace();
        }

    }

    @ApiOperation("付款信息查询")
    @RequestMapping("/query")
    public @ResponseBody Object queryOrder(@RequestParam Integer orderID) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        try {
            String out_trade_no = new String(orderID.toString().getBytes("ISO-8859-1"), "UTF-8");

            //商户订单号，商户网站订单系统中唯一订单号
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"}");

            //请求
            //String result = alipayClient.execute(alipayRequest).getBody();
            //return alipayService.queryOrder(running);
            AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);
            if(response.isSuccess()){
                System.out.println("[Alipay Query] 调用成功");

                return alipayService.queryOrderFromAlipay(orderID, response);
            } else {
                System.out.println("[Alipay Query] 调用失败" + response.getMsg());
                return ResultUtil.error(-2, "调用失败：" + response.getMsg());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(-1, e.getMessage());
        }
    }

    @ApiOperation("查询最后一个订单状态")
    @RequestMapping("/last-order")
    public @ResponseBody Object queryLastOrder() {
        return alipayService.queryLastOrder();
    }

}
