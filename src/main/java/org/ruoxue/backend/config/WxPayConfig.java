package org.ruoxue.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WxPayConfig {

    @Value("${wxpay.addid}")
    public static String APP_ID;

    @Value("${wxpay.mchid}")
    public static String MCH_ID;

    @Value("${wxpay.key}")
    public static String KEY;

    @Value("${wxpay.cert-path}")
    public static String CERT_PATH;

    @Value("${wxpay.configured}")
    public static Boolean WX_CONFIGURED;

    @Value("${wxpay.wxpay.notify-url}")
    public static String NOTIFY_URL;
}
