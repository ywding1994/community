package com.ywding1994.community.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ywding1994.community.controller.interceptor.LoginRequiredInterceptor;
import com.ywding1994.community.controller.interceptor.LoginTicketInterceptor;
import com.ywding1994.community.controller.interceptor.MessageInterceptor;

/**
 * SpringMVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

        @Resource
        private LoginTicketInterceptor loginTicketInterceptor;

        @Resource
        private LoginRequiredInterceptor loginRequiredInterceptor;

        @Resource
        private MessageInterceptor messageInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(loginTicketInterceptor).excludePathPatterns("/**/*.css", "/**/*.js",
                                "/**/*.png",
                                "/**/*.jpeg");
                registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/**/*.css", "/**/*.js",
                                "/**/*.png",
                                "/**/*.jpg", "/**/*.jpeg");
                registry.addInterceptor(messageInterceptor).excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png",
                                "/**/*.jpg", "/**/*.jpeg");
        }

}
