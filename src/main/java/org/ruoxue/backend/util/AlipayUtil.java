package org.ruoxue.backend.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.ruoxue.backend.common.constant.Constant;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;

import static org.ruoxue.backend.config.AlipayConfig.*;
import static org.ruoxue.backend.config.AlipayConfig.CHARSET;

public class AlipayUtil {

    public static void startPayment(String orderID, String goodName, Double fee,  HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTOFY_URL);//在公共参数中设置回跳和通知地址
        String passback = URLEncoder.encode("id="+PROVIDER_ID+"&order="+orderID);
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderID + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+ fee +"," +
                "    \"subject\":\"" + goodName + "\"," +
                "    \"body\":\"" + goodName + "\"," +
                "    \"passback_params\":\"" + passback + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + PROVIDER_ID + "\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            httpResponse.setContentType("text/html;charset=" + CHARSET);
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
