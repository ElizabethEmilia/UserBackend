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

    @Bean
    public UserBackendInterceptor getUserBackendInterceptor() {
        return new UserBackendInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getUserBackendInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/api/login");

        // 拦截配置
        addInterceptor.addPathPatterns("/api/**");
    }

}
