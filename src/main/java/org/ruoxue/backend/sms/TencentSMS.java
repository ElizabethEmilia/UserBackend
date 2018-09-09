package org.ruoxue.backend.sms;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;

public class TencentSMS {
    public static boolean initialized = false;

    // 短信应用SDK AppKey
    public static String appkey = "";

    // 短信应用SDK AppID
    public static int appid = 0;

    // 签名
    public static String smsSign = "";

    // 短信模板ID
    public static int templateId = 0;

    // 模板內容
    public static String template = "";

    public static void sendTextMessage(String phone, String templates) throws Exception {
        if (!initialized) {
            throw new Exception("Parameters is not initialized.");
        }
        String[] params = templates.split(",");
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    templateId, params, smsSign, "", "");
        System.out.println(result);
    }

}
