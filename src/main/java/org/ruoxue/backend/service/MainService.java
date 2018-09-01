package org.ruoxue.backend.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface MainService {

    Object login(JSONObject jsonObject);

    void gerenateVerifycode(HttpServletRequest request, HttpServletResponse response);

}
