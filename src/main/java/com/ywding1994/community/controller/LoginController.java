package com.ywding1994.community.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "注册和登录接口")
public class LoginController {

    @Resource
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    @ApiOperation(value = "请求注册页面", httpMethod = "GET")
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "注册账号", httpMethod = "POST")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (MapUtils.isEmpty(map)) {
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

}
