package org.ruoxue.backend.util;


import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.feature.PermissionManager;
import org.ruoxue.backend.mapper.TAdminMapper;
import org.ruoxue.backend.mapper.TRoleMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *  志勋 + 佳斌 工具类
 */
public class XunBinKit {

    @Resource
    private TAdminMapper adminMapper;

    @Resource
    private PermissionManager permissionManager;

    @Resource
    private TRoleMapper roleMapper;

    /**
     *  权限获取role的value值
     */
    public Integer getPermission(){
        Integer uid = getUid();

        if (ToolUtil.isEmpty(uid)) {
            return 0;
        }

        TAdmin admin = adminMapper.getTAdminByUid(uid);

        Integer roleId = admin.getRoleid();

        Integer value = roleMapper.getRoleById(roleId).getValue();

        return value;
    }



    /**
     *  获取时间戳+三位随机数
     */
    public static Long getTimeAppendThreeNum(){

        Date date = new Date();
        Long time = date.getTime();

        String threeNum = XunBinKit.generateNum(3);


        return Long.valueOf(time + "" + threeNum);
    }

    /**
     *  获取年底时间
     */
    public static Date getYearLastTime() {
        String time = Calendar.getInstance().get(Calendar.YEAR) + "-12" + "-31 " + "23:59:59";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     *  处理异常，返回状态码
     */
    public static void returnCode(Integer code, String msg){
        HttpServletResponse response = XunBinKit.getResponse();
        response.setStatus(code);
        try {
            response.getWriter().write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
     *  获取用户session对象
     */
    public static HttpSession getSession(){
        HttpSession session = HttpKit.getRequest().getSession();
        return session;
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
        return Md5Util.getMD5(sixNum);
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

    /**
     *  随机生成n位数字
     */
    public static String generateNum(Integer n){
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < n; i++){
            int num = rand.nextInt(10);
            sb.append(num + "");
        }
        return sb.toString();
    }

}
