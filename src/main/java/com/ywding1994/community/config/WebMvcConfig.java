package com.ywding1994.community.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ywding1994.community.controller.interceptor.LoginTicketInterceptor;

/**
 * SpringMVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor).excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png",
                "/**/*.jpeg");
    }

}
