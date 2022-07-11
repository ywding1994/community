package com.ywding1994.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.ywding1994.community.constant.CommentConstant;
import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.constant.HTTPStatusCodeConstant;
import com.ywding1994.community.entity.Comment;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.CommentService;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.service.LikeService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.CommunityUtil;
import com.ywding1994.community.util.HostHolder;
import com.ywding1994.community.vo.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/discuss")
@Api(tags = "讨论帖接口")
public class DiscussPostController {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    @Resource
    private CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "发布讨论帖", httpMethod = "POST")
    public String addDiscussPost(@RequestParam @ApiParam("标题") String title,
            @RequestParam @ApiParam("内容") String content) {
        // 校验登录状态
        User user = hostHolder.getUser();
        if (Objects.isNull(user)) {
            return CommunityUtil.getJSONString(HTTPStatusCodeConstant.FORBIDDEN, "用户未登录！");
        }

        DiscussPost discussPost = DiscussPost.builder().userId(user.getId()).title(title).content(content).build();
        if (discussPostService.addDiscussPost(discussPost)) {
            return CommunityUtil.getJSONString(HTTPStatusCodeConstant.OK, "讨论帖发布成功！");
        } else {
            return CommunityUtil.getJSONString(HTTPStatusCodeConstant.INTERNAL_SERVER_ERROR, "讨论帖发布失败！");
        }
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    @ApiOperation(value = "请求讨论帖详情页", httpMethod = "GET")
    public String getDiscussPost(Model model, @PathVariable("discussPostId") int discussPostId, Page page) {
        // 查询讨论帖
        DiscussPost discussPost = discussPostService.getById(discussPostId);
        model.addAttribute("post", discussPost);

        // 获取作者信息
        User user = userService.getById(discussPost.getUserId());
        model.addAttribute("user", user);

        // 获取点赞情况
        // 点赞数
        long likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        // 点赞状态
        int likeStatus = Objects.isNull(hostHolder.getUser()) ? 0
                : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), CommunityConstant.ENTITY_TYPE_POST,
                        discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 设置评论分页信息
        page.setLimit(5);
        page.setRows(discussPost.getCommentCount());
        page.setPath("/discuss/detail/" + discussPostId);

        // 评论列表
        List<Comment> comments = commentService.findCommentsByEntity(CommentConstant.EntityType.ENTITY_TYPE_POST,
                discussPost.getId(), page.getCurrent(), page.getLimit());
        // 评论VO列表
        List<Map<String, Object>> commentVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(comments)) {
            for (Comment comment : comments) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.getById(comment.getUserId()));
                // 点赞数
                likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                // 点赞状态
                likeStatus = Objects.isNull(hostHolder.getUser()) ? 0
                        : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),
                                CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

                // 回复列表
                List<Comment> replys = commentService.findCommentsByEntity(
                        CommentConstant.EntityType.ENTITY_TYPE_COMMENT, comment.getId(), 1, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVos = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(replys)) {
                    for (Comment reply : replys) {
                        // 回复VO
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.getById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.getById(reply.getTargetId());
                        replyVo.put("target", target);
                        // 点赞数
                        likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_COMMENT,
                                reply.getId());
                        // 点赞状态
                        likeStatus = Objects.isNull(hostHolder.getUser()) ? 0
                                : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),
                                        CommunityConstant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVos.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVos);

                // 回复数量
                int replyCount = commentService.findCommentCount(CommentConstant.EntityType.ENTITY_TYPE_COMMENT,
                        comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVos.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVos);
        return "/site/discuss-detail";
    }

}
