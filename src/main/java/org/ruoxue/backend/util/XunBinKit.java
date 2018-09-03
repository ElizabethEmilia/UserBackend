package org.ruoxue.backend.util;


import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *  志勋 + 佳斌 工具类
 */
public class XunBinKit {

    /**
     *  获取response对象
     */
    public static HttpServletResponse getResponse(){
        HttpServletResponse response = (HttpServletResponse) RequestContextHolder.getRequestAttributes();
        return response;
    }

    /**
     *  生成一个md加密后的64为token
     */
    public static String generateToken(){
        String sixNum = generateSixNum();
        String md5Token = null;
        try {
            md5Token = Md5SaltTool.getEncryptedPwd(sixNum);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5Token;
    }

    /**
     *  随机生成六位数字
     */
    public static String generateSixNum(){
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < 6; i++){
            int num = rand.nextInt(10);
            sb.append(num + "");
        }
        return sb.toString();
    }

}
