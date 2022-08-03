package com.ywding1994.community.controller.interceptor;

import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.MessageService;
import com.ywding1994.community.util.HostHolder;

/**
 * 未读消息拦截器
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (Objects.nonNull(user) && Objects.nonNull(modelAndView)) {
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
            int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
            modelAndView.addObject("allUnreadCount", letterUnreadCount + noticeUnreadCount);
        }
    }

}
