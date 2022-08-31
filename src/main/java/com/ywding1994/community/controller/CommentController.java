package com.ywding1994.community.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ywding1994.community.constant.CommentConstant;
import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.entity.Comment;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.event.Event;
import com.ywding1994.community.event.EventProducer;
import com.ywding1994.community.service.CommentService;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.util.HostHolder;
import com.ywding1994.community.util.RedisKeyUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/comment")
@Api(tags = "评论接口")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private EventProducer eventProducer;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    @ApiOperation(value = "发布评论", httpMethod = "POST")
    public String addComment(@PathVariable("discussPostId") @ApiParam("讨论帖id") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(CommentConstant.Status.NORMAL);
        commentService.addComment(comment);

        // 触发评论事件
        Event event = Event.builder()
                .topic(CommunityConstant.TOPIC_COMMENT)
                .userId(hostHolder.getUser().getId())
                .entityType(comment.getEntityType())
                .entityId(comment.getEntityId())
                .data(Map.of("postId", discussPostId))
                .build();
        if (comment.getEntityType() == CommunityConstant.ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.getById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == CommunityConstant.ENTITY_TYPE_COMMENT) {
            Comment target = commentService.getById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == CommunityConstant.ENTITY_TYPE_POST) {
            // 触发发帖事件
            event = Event.builder()
                    .topic(CommunityConstant.TOPIC_PUBLISH)
                    .userId(comment.getUserId())
                    .entityType(CommunityConstant.ENTITY_TYPE_POST)
                    .entityId(discussPostId)
                    .build();
            eventProducer.fireEvent(event);

            // 计算讨论帖分数并存入Redis
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, discussPostId);
        }
        return "redirect:/discuss/detail/" + discussPostId;
    }

}
