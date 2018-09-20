package org.ruoxue.backend.config.interceptor;

import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.util.CookieUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  springboot自定义拦截器
 *
 * @author fengjb
 * @date 2018-09-04
 */
@Component
public class UserBackendInterceptor extends BaseController implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(UserBackendInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        logger.info("自定义拦截器......");

        getSession().setAttribute("uid", 1);

//        判断cookie中是否有jsession
        Cookie cookie = CookieUtil.getCookie(request, "JSESSIONID");
//        加响应头
        if(ToolUtil.isEmpty(cookie)){
            response.setHeader("Set-Cookie", "JSESSIONID=" + request.getSession().getId());
        }
        response.setHeader("Allow-Cross-Origin", "*");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
