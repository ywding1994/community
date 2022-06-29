package com.ywding1994.community.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.code.kaptcha.Producer;
import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Slf4j
@Api(tags = "注册和登录接口")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

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

    @RequestMapping(path = "/activation/{id}/{code}", method = RequestMethod.GET)
    @ApiOperation(value = "激活账号", httpMethod = "GET")
    public String activation(Model model, @PathVariable("id") @ApiParam("用户id") int userId,
            @PathVariable("code") @ApiParam("激活码") String activationCode) {
        int result = userService.activation(userId, activationCode);
        switch (result) {
            case CommunityConstant.ACTIVATION_SUCCESS:
                model.addAttribute("msg", "激活成功，您的账号已经可以正常使用了！");
                model.addAttribute("target", "/login");
                break;
            case CommunityConstant.ACTIVATION_REPEAT:
                model.addAttribute("msg", "无效操作，该账号已经激活过了！");
                model.addAttribute("target", "/index");
                break;
            case CommunityConstant.ACTIVATION_FAILURE:
                model.addAttribute("msg", "激活失败，您提供的激活码不正确！");
                model.addAttribute("target", "/index");
                break;
            default:
                log.error("Activation Error: invalid activation result, please check!");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    @ApiOperation(value = "请求登录页面", httpMethod = "GET")
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    public String login(Model model, HttpServletResponse response, @ApiIgnore HttpSession session,
            @RequestParam @ApiParam("用户名") String username,
            @RequestParam @ApiParam("密码") String password,
            @RequestParam @ApiParam("验证码") String code,
            @RequestParam(defaultValue = "false") @ApiParam(value = "记住我", defaultValue = "false") boolean rememberMe) {
        // 校验验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确！");
            return "/site/login";
        }

        // 校验账号、密码
        int expiredSeconds = rememberMe ? CommunityConstant.REMEMBER_EXPIRED_SECONDS
                : CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码", httpMethod = "GET")
    public void getKaptcha(HttpServletResponse response, @ApiIgnore HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将图片输出到浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            log.error("Kaptcha Generation Failed: " + e.getMessage());
        }
    }

}
