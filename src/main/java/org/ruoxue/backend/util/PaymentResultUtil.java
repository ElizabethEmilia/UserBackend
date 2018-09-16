package org.ruoxue.backend.util;

import java.io.File;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PaymentResultUtil {

    public static HashMap<String, String> getTokens(String out_trade_no,
                                                    String total_amount,
                                                    String trade_no,
                                                    String timestamp,
                                                    String pay_method) {
        HashMap<String, String> map = new HashMap<String, String>();

//        将参数显示
        map.put("out_trade_no", out_trade_no);
        map.put("total_amount", total_amount);
        map.put("trade_no", trade_no);
        map.put("timestamp", timestamp);
        map.put("pay_method", pay_method);
        return map;
    }

    public static String paymentResult(HashMap<String, String> tokens) throws Exception {
        String template = new String(IOUtil.read(System.getProperty("user.dir") + "/src/main/resources/res/pay-finish-template.html"), UTF_8);
        String parsed = TemplateUtil.parseTemplate(tokens, template);
        System.out.println(parsed);
        return parsed;
    }

    public static String paymentResult(String out_trade_no,
                                       String total_amount,
                                       String trade_no,
                                       String timestamp,
                                       String pay_method) throws Exception {
        return paymentResult(getTokens(out_trade_no, total_amount, trade_no, timestamp, pay_method));
    }

}
