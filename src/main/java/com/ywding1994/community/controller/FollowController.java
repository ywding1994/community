package com.ywding1994.community.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.constant.HTTPStatusCodeConstant;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.event.Event;
import com.ywding1994.community.event.EventProducer;
import com.ywding1994.community.service.FollowService;
import com.ywding1994.community.util.CommunityUtil;
import com.ywding1994.community.util.HostHolder;
import com.ywding1994.community.vo.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "关注接口")
public class FollowController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private FollowService followService;

    @Resource
    private EventProducer eventProducer;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "进行关注", httpMethod = "POST")
    public String follow(@RequestParam @ApiParam("实体类型") int entityType, @RequestParam @ApiParam("实体id") int entityId) {
        User user = hostHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);

        // 触发关注事件
        Event event = Event.builder()
                .topic(CommunityConstant.TOPIC_FOLLOW)
                .userId(hostHolder.getUser().getId())
                .entityType(entityType)
                .entityId(entityId)
                .entityUserId(entityId)
                .build();
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(HTTPStatusCodeConstant.OK, "已关注！");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "取消关注", httpMethod = "POST")
    public String unfollow(@RequestParam @ApiParam("实体类型") int entityType,
            @RequestParam @ApiParam("实体id") int entityId) {
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(HTTPStatusCodeConstant.OK, "已取消关注！");
    }

    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "请求关注用户详情页", httpMethod = "GET")
    public String getFollowees(Model model, @PathVariable("userId") @ApiParam("用户id") int userId, Page page) {
        User user = hostHolder.getUser();
        if (Objects.isNull(user)) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user", user);

        // 设置分页信息
        page.setLimit(5);
        page.setRows((int) followService.findFolloweeCount(userId, CommunityConstant.ENTITY_TYPE_USER));
        page.setPath("/followees/" + userId);

        // 关注用户列表
        List<Map<String, Object>> userMaps = followService.findFollowees(userId, page.getCurrent(), page.getLimit());
        if (CollectionUtils.isNotEmpty(userMaps)) {
            for (Map<String, Object> map : userMaps) {
                User followeeUser = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(followeeUser.getId()));
            }
        }
        model.addAttribute("userMaps", userMaps);
        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "请求关注者详情页", httpMethod = "GET")
    public String getFollowers(Model model, @PathVariable("userId") @ApiParam("用户id") int userId, Page page) {
        User user = hostHolder.getUser();
        if (Objects.isNull(user)) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user", user);

        // 设置分页信息
        page.setLimit(5);
        page.setRows((int) followService.findFollowerCount(userId, CommunityConstant.ENTITY_TYPE_USER));
        page.setPath("/followers/" + userId);

        // 关注者列表
        List<Map<String, Object>> userMaps = followService.findFollowers(userId, page.getCurrent(), page.getLimit());
        if (CollectionUtils.isNotEmpty(userMaps)) {
            for (Map<String, Object> map : userMaps) {
                User followeeUser = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(followeeUser.getId()));
            }
        }
        model.addAttribute("userMaps", userMaps);
        return "/site/follower";
    }

    /**
     * 查询当前用户是否已关注指定用户
     *
     * @param userId 用户id
     * @return 关注状态（已关注：true，未关注：false）
     */
    private boolean hasFollowed(int userId) {
        if (Objects.isNull(hostHolder.getUser())) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), CommunityConstant.ENTITY_TYPE_USER, userId);
    }

}
