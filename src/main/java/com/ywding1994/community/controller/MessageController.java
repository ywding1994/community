package com.ywding1994.community.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.ywding1994.community.service.MessageService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.HostHolder;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@Api(tags = "消息接口")
public class MessageController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

}
