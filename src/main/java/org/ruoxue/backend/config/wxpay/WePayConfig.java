package org.ruoxue.backend.config.wxpay;

import com.github.wxpay.sdk.WXPayConfig;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.config.WxPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WePayConfig implements WXPayConfig {

    private byte[] certData;

    public WePayConfig() throws Exception {
        String certPath = WxPayConfig.CERT_PATH;
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return WxPayConfig.APP_ID;
    }

    public String getMchID() {
        return WxPayConfig.MCH_ID;
    }

    public String getKey() {
        return WxPayConfig.KEY;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

}
