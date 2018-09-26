package org.ruoxue.backend.config.interceptor;

import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.util.CookieUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  springboot自定义拦截器
 *
 * @author fengjb
 * @date 2018-09-04
 */
@Component
public class UserBackendInterceptor extends BaseController implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(UserBackendInterceptor.class);

    Pattern pattern = Pattern.compile("^\\/api\\/(\\w+)\\/");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        logger.info("[Interceptor] " + request.getRequestURI());

//        判断cookie中是否有jsession
        Cookie cookie = CookieUtil.getCookie(request, "JSESSIONID");
//        加响应头
        if(ToolUtil.isEmpty(cookie)){
            response.setHeader("Set-Cookie", "JSESSIONID=" + request.getSession().getId());
        }
        response.setHeader("Allow-Cross-Origin", "*");

        Matcher matcher = pattern.matcher(request.getRequestURI());

        if (matcher.find()) {
            String part = matcher.group(1);
            // 检查管理员接口是否具有管理员权限
            if (part.equals("customer") || part.equals("system")) {
                if (XunBinKit.thisModuleRequiresAdminButThisUserIsNot()) {
                    logger.info("[Interceptor] [REJECT(REQUIRE_ADMIN)]" + request.getRequestURI());
                    return false;
                }
            }
            // 放行支付接口
            if (part.equals("pay")) {
                return true;
            }
            // 检查用户接口是否登陆
            if (XunBinKit.thisModuleRequiresLoginButDidNot()) {
                logger.info("[Interceptor] [REJECT(REQUIRE_LOGIN)]" + request.getRequestURI());
                return false;
            }
            return true;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
