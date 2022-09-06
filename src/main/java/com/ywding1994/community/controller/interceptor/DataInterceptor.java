package com.ywding1994.community.controller.interceptor;

import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.DataService;
import com.ywding1994.community.util.HostHolder;

@Component
public class DataInterceptor implements HandlerInterceptor {

    @Resource
    private DataService dataService;

    @Resource
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 统计UV
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        // 统计DAU
        User user = hostHolder.getUser();
        if (Objects.nonNull(user)) {
            dataService.recordDAU(user.getId());
        }
        return true;
    }

}
