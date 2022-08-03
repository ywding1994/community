package com.ywding1994.community.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.constant.HTTPStatusCodeConstant;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.event.Event;
import com.ywding1994.community.event.EventProducer;
import com.ywding1994.community.service.LikeService;
import com.ywding1994.community.util.CommunityUtil;
import com.ywding1994.community.util.HostHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "点赞接口")
public class LikeController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private LikeService likeService;

    @Resource
    private EventProducer eventProducer;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "进行点赞", httpMethod = "POST")
    public String like(
            @RequestParam @ApiParam("实体类型") int entityType,
            @RequestParam @ApiParam("实体id") int entityId,
            @RequestParam @ApiParam("实体对应的用户id") int entityUserId,
            @RequestParam @ApiParam("讨论帖id") int postId) {
        User user = hostHolder.getUser();

        // 进行点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 点赞状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        // 返回点赞结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = Event.builder()
                    .topic(CommunityConstant.TOPIC_LIKE)
                    .userId(hostHolder.getUser().getId())
                    .entityType(entityType)
                    .entityId(entityId)
                    .entityUserId(entityUserId)
                    .data(Map.of("postId", postId))
                    .build();
            eventProducer.fireEvent(event);
        }

        return CommunityUtil.getJSONString(HTTPStatusCodeConstant.OK, null, map);
    }

}
