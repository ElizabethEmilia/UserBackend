package org.ruoxue.backend.util;

/**
 *  封装结果工具类
 */
public class ResultUtil {

    /**
     * 返回成功
     * @param data
     * @return
     */
    public static Result success(Object data){
        Result rs = new Result();
        rs.setCode(0);
        rs.setMsg("成功");
        rs.setData(data);
        return rs;
    }

    public static Result success(){
       return success(null);
    }

    /**
     * 返回自己想要的提示
     * @param code
     * @param msg
     * @return
     */
    public static Result success(Integer code,String msg){
        Result rs = new Result();
        rs.setCode(code);
        rs.setMsg(msg);
        return rs;
    }

    /**
     * 返回错误
     * @param code
     * @param msg
     * @return
     */
    public static Result error(Integer code,String msg){
        Result rs = new Result();
        rs.setCode(code);
        rs.setMsg(msg);
        return rs;
    }
    
    /**
     * 
     * @param code
     * @param data
     * @param msg
     * @return
     */
    public static Result result(Integer code,Object data,String msg){
        Result rs = new Result();
        rs.setCode(code);
        rs.setData(data);
        rs.setMsg(msg);
        return rs;
    }
}
