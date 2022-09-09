package com.ywding1994.community.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.ElasticsearchService;
import com.ywding1994.community.service.LikeService;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.vo.Page;
import com.ywding1994.community.vo.SearchResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "搜索接口")
public class SearchController {

    @Resource
    private ElasticsearchService elasticsearchService;

    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "搜索讨论帖", httpMethod = "GET")
    public String search(Model model, Page page, @RequestParam @ApiParam("查询关键词") String keyword) throws IOException {
        // 搜索贴子
        SearchResult searchResult = elasticsearchService.searchDiscussPost(keyword,
                (page.getCurrent() - 1) * page.getLimit(), page.getLimit());

        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        List<DiscussPost> list = searchResult.getList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DiscussPost discussPost : list) {
                Map<String, Object> map = new HashMap<>();
                // 讨论帖
                map.put("post", discussPost);
                // 作者
                map.put("user", userService.getUserById(discussPost.getUserId()));
                // 点赞数量
                map.put("likeCount",
                        likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, discussPost.getId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);

        // 设置分页信息
        page.setRows(Objects.isNull(searchResult) ? 0 : (int) searchResult.getTotal());
        page.setPath("/search?keyword=" + keyword);
        return "/site/search";
    }

}
