package com.ywding1994.community.controller.interceptor;

import java.util.Date;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ywding1994.community.constant.LoginTicketConstant;
import com.ywding1994.community.entity.LoginTicket;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.LoginTicketService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.CookieUtil;
import com.ywding1994.community.util.HostHolder;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Resource
    private LoginTicketService loginTicketService;

    @Resource
    private UserService userService;

    @Resource
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 从Cookie中获取登录凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (Objects.nonNull(ticket)) {
            // 查询登录凭证
            LoginTicket loginTicket = loginTicketService.findLoginTicket(ticket);

            // 校验登录凭证
            if (Objects.nonNull(loginTicket) && loginTicket.getStatus() == LoginTicketConstant.Status.VALID
                    && loginTicket.getExpired().after(new Date())) {
                // 根据登录凭证查询用户
                User user = userService.getById(loginTicket.getUserId());
                // 在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (Objects.nonNull(user) && Objects.nonNull(modelAndView)) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        hostHolder.clear();
    }

}
