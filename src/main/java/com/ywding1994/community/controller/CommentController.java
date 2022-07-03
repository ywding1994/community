package com.ywding1994.community.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ywding1994.community.constant.CommentConstant;
import com.ywding1994.community.entity.Comment;
import com.ywding1994.community.service.CommentService;
import com.ywding1994.community.util.HostHolder;

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

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    @ApiOperation(value = "发布评论", httpMethod = "POST")
    public String addComment(@PathVariable("discussPostId") @ApiParam("讨论帖id") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(CommentConstant.Status.NORMAL);
        commentService.addComment(comment);
        return "redirect:/discuss/detail/" + discussPostId;
    }

}
