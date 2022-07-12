package com.ywding1994.community.controller;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.FollowService;
import com.ywding1994.community.service.LikeService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.HostHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(path = "/user")
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
