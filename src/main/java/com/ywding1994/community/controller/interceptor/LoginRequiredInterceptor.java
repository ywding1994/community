package com.ywding1994.community.controller.interceptor;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ywding1994.community.annotation.LoginRequired;
import com.ywding1994.community.util.HostHolder;

/**
 * 登录鉴权拦截器
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Resource
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 判断handler是否为方法
        if (handler instanceof HandlerMethod) {
            // 获取方法中的LoginRequired注解和当前用户
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);

            // 判断是否需要拦截并重定向至登录页面
            if (Objects.nonNull(loginRequired) && Objects.isNull(hostHolder.getUser())) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }

}
