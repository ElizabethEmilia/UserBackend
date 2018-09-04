package org.ruoxue.backend.config.interceptor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *  配置过滤器
 */
@SpringBootApplication
public class MukiyBinConfig extends WebMvcConfigurerAdapter {

    /**
     * 登录session key
     */
    public static final String SESSION_KEY = "uid";

    @Bean
    public UserBackendInterceptor getSecurityInterceptor() {
        return new UserBackendInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
//        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/api/login");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }
}
