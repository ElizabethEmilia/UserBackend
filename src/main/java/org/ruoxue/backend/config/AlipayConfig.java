package org.ruoxue.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class AlipayConfig {

    //@Value("${alipay.appid}")
    public static String APP_ID = null;

    //@Value("${alipay.gateway}")
    public static String GATEWAY;

    //@Value("${alipay.app-private-key}")
    public static String APP_PRIVATE_KEY;

    public static String FORMAT = "json";

    public static String CHARSET = "UTF-8";

    //@Value("${alipay.app-public-key}")
    public static String ALIPAY_PUBLIC_KEY ;

    //@Value("${alipay.sign-type}")
    public static String SIGN_TYPE;

    //@Value("${alipay.provider-id}")
    public static String PROVIDER_ID;

    //@Value("${alipay.return-url}")
    public static String RETURN_URL;

    //@Value("${alipay.notify-url}")
    public static String NOTOFY_URL;

}
