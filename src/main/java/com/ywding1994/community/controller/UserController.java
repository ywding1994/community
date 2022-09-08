package com.ywding1994.community.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ywding1994.community.annotation.LoginRequired;
import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.FollowService;
import com.ywding1994.community.service.LikeService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.CommunityUtil;
import com.ywding1994.community.util.HostHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(path = "/user")
@Slf4j
@Api(tags = "用户接口")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    @Resource
    private FollowService followService;

    @Resource
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    @ApiOperation(value = "请求设置页面", httpMethod = "GET")
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    @ApiOperation(value = "上传头像", httpMethod = "POST")
    public String uploadHeader(Model model, @RequestParam @ApiParam("头像图片") MultipartFile headerImage) {
        if (Objects.isNull(headerImage)) {
            model.addAttribute("error", "您还没有选择图片！");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确！");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 上传文件
        File dest = new File(uploadPath, fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("File Upload Failed: " + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }

        // 更新当前用户头像URL地址（web访问路径）
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.update(
                new LambdaUpdateWrapper<>(User.class).eq(User::getId, user.getId()).set(User::getHeaderUrl, headerUrl));
        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    @ApiOperation(value = "获取头像", httpMethod = "GET")
    public void getHeader(HttpServletResponse response, @PathVariable("fileName") @ApiParam("文件名") String fileName) {
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);

        try (
                FileInputStream fileInputStream = new FileInputStream(fileName);
                OutputStream outputStream = response.getOutputStream();) {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("Header Image Read Failed: " + e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码", httpMethod = "POST")
    public String updatePassword(Model model,
            @ApiParam("原始密码") String originalPassword,
            @ApiParam("新密码") String newPassword,
            @ApiParam("确认密码") String confirmPassword) {
        // 参数校验
        // 校验空值
        if (StringUtils.isBlank(originalPassword)) {
            model.addAttribute("originalPasswordMsg", "请输入原始密码！");
            return "site/setting";
        }
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordMsg", "请输入新密码！");
            return "site/setting";
        }
        if (StringUtils.isBlank(confirmPassword)) {
            model.addAttribute("confirmPasswordMsg", "请再次输入新密码！");
            return "site/setting";
        }
        // 校验原始密码是否正确
        User user = hostHolder.getUser();
        if (!CommunityUtil.md5(originalPassword + user.getSalt()).equals(user.getPassword())) {
            model.addAttribute("originalPasswordMsg", "密码错误！");
            return "site/setting";
        }
        // 校验两次新密码是否输入一致
        if (!confirmPassword.equals(newPassword)) {
            model.addAttribute("confirmPasswordMsg", "两次输入的密码不一致！");
            return "site/setting";
        }
        // 校验新旧密码是否不同
        if (newPassword.equals(originalPassword)) {
            model.addAttribute("newPasswordMsg", "新密码不能与原始密码相同");
            return "site/setting";
        }

        // 修改密码并重定向到主页
        userService.updatePassword(user.getId(), CommunityUtil.md5(newPassword + user.getSalt()));
        return "redirect:/index";
    }

    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "请求个人主页", httpMethod = "GET")
    public String getProfilePage(Model model, @PathVariable("userId") @ApiParam("用户id") int userId) {
        User user = userService.getById(userId);
        if (Objects.isNull(user)) {
            throw new RuntimeException("该用户不存在！");
        }

        // 用户
        model.addAttribute("user", user);

        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, CommunityConstant.ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);

        // 关注者数量
        long followerCount = followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);

        // 是否已被当前用户关注
        boolean hasFollowed = false;
        if (Objects.nonNull(hostHolder.getUser())) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), CommunityConstant.ENTITY_TYPE_USER,
                    userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        return "/site/profile";
    }

}
