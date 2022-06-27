package com.ywding1994.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.vo.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "主页接口")
public class HomeController {

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    @ApiOperation(value = "请求主页", httpMethod = "GET")
    public String getIndexPage(Model model, Page page) {
        // 设置分页信息
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        // 分页查询所有用户所发的帖子并显示
        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, page.getCurrent(), page.getLimit());
        List<Map<String, Object>> discussPostMaps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(discussPosts)) {
            for (DiscussPost discussPost : discussPosts) {
                Map<String, Object> map = new HashMap<>();
                User user = userService.getById(discussPost.getUserId());
                map.put("post", discussPost);
                map.put("user", user);
                discussPostMaps.add(map);
            }
        }

        model.addAttribute("discussPostMaps", discussPostMaps);
        return "/index";
    }

}
