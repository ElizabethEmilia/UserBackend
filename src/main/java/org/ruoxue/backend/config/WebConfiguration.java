//package org.ruoxue.backend.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//public class WebConfiguration extends WebMvcConfigurerAdapter {
//
//    private final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        logger.info("Add interceptors");
//        registry.addInterceptor(new LoginRequireInterceptor());
//        registry.addInterceptor(new PrivilegeInterceptor());
//    }
//
//}
