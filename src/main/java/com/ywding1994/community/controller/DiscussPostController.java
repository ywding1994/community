package com.ywding1994.community.controller;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ywding1994.community.constant.HTTPStatusCodeConstant;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.util.CommunityUtil;
import com.ywding1994.community.util.HostHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/discuss")
@Api(tags = "讨论帖接口")
public class DiscussPostController {

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private HostHolder hostHolder;

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

}
