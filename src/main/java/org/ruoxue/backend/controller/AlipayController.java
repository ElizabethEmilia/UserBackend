package org.ruoxue.backend.controller;

import com.alipay.api.internal.util.AlipaySignature;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.service.ITLogsService;
import org.ruoxue.backend.util.AlipayUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.ruoxue.backend.common.constant.Constant.AlipayConfig.CHARSET;

@RestController
@RequestMapping("/api/pay/alipay/")
public class AlipayController {

    @Resource
    ITLogsService logsService;

    @RequestMapping("start")
    public void startPayment(String orderID, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        AlipayUtil.startPayment(orderID, "iPhone XS Max 512GB", 14000.00, httpRequest, httpResponse );
    }

    /// 这个函数是回调  但是不一定确实成功了，只是给用户显示的界面
    /// 结果以下面那个notify为准
    @RequestMapping("finsih")
    public void finishPaymant(@RequestParam("out_trade_no") String out_trade_no,
                                       @RequestParam("total_amount") Double total_amount,
                                       @RequestParam("sign") String sign,
                                       @RequestParam("trade_no") String trade_no,
                                       @RequestParam("auth_app_id") String auth_app_id,
                                       @RequestParam("app_id") String app_id,
                                       @RequestParam("seller_id") String seller_id,
                                       @RequestParam("timestamp") String timestamp,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        try {
            // 避免其他人来自非本站的请作为回调
            if (!seller_id.equals(Constant.AlipayConfig.PROVIDER_ID)) {
                response.setStatus(403);
                return;
            }

            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("Success!! Trade Number: " + trade_no + "; Payment Amount: " + total_amount);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("notify")
    public void notifyQuery(@RequestParam HashMap<String, String> params, HttpServletResponse response) {
        Map<String, String> paramsMap = params;
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, Constant.AlipayConfig.ALIPAY_PUBLIC_KEY, CHARSET, Constant.AlipayConfig.SIGN_TYPE); //调用SDK验证签名
            if(signVerified){
                // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
                response.getWriter().println("success");
            }else{
                // TODO 验签失败则记录异常日志，并在response中返回failure.
                response.getWriter().println("failure");

                response.getOutputStream().println("非法访问者你好，你的APP ID我们已经记下，我们将保存这个请求并向支付宝举报。");
                logsService.actionLogNow(Constant.LogUsers.SYSTEM,
                        "非法的支付宝API接口请求: " + params.toString(),
                        Constant.LogClass.DANGEROUS);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
