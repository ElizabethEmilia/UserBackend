package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface MainService {

    Object login(JSONObject jsonObject) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    void gerenateVerifycode(HttpServletRequest request, HttpServletResponse response);

    Object forgetPwd(JSONObject jsonObject);

    Object resetpwd(JSONObject jsonObject);

}
