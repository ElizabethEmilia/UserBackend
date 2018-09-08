package org.ruoxue.backend.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *  志勋 + 佳斌 工具类
 */
public class XunBinKit {

    /**
     *  封装返回结果
     */
    public static Object returnResult(boolean flag, Integer code, Object data, String succMsg, String errMsg){
        if(flag){
            return ResultUtil.result(0, data, succMsg);
        } else {
            return ResultUtil.error(code, errMsg);
        }
    }

    /**
     *  封装非空验证
     */
    public static Object isEmpty(Object ... param){
        if(ToolUtil.isEmpty(param)){
            return ResultUtil.error(-1, "参数错误");
        } else {
            return null;
        }
    }

    /**
     *  处理公司模块所有的uid
     */
    public static Integer getUidByString(String uid){
        Integer userid = 0;
        if("_".equals(uid)){
            userid = -1;
        } else {
            userid = Integer.parseInt(uid);
        }
        return userid;
    }

    /**
     *  获取用户id
     */
    public static Integer getUid(){
        HttpSession session = HttpKit.getRequest().getSession();
        Integer uid = (Integer) session.getAttribute("uid");
        return uid;
    }

    /**
     *  获取response对象
     */
    public static HttpServletResponse getResponse(){
        HttpServletResponse response = HttpKit.getResponse();
        return response;
    }

    /**
     *  获取response对象
     */
    public static HttpServletRequest getRequest(){
        HttpServletRequest request = HttpKit.getRequest();
        return request;
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
