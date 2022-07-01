package com.ywding1994.community.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.DiscussPostConstant;
import com.ywding1994.community.dao.DiscussPostMapper;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.util.SensitiveFilter;

@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost> implements DiscussPostService {

    @Resource
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<DiscussPost> findDiscussPosts(int userId, int current, int limit) {
        Page<DiscussPost> page = new Page<>(current, limit, false);
        page = this.page(page,
                new LambdaQueryWrapper<>(DiscussPost.class)
                        .ne(DiscussPost::getStatus, DiscussPostConstant.Status.BLOCKED)
                        .eq(userId != 0, DiscussPost::getUserId, userId)
                        .orderByDesc(Arrays.asList(DiscussPost::getType, DiscussPost::getCreateTime)));
        return page.getRecords();
    }

    @Override
    public int findDiscussPostRows(int userId) {
        return (int) this.count(new LambdaQueryWrapper<>(DiscussPost.class).select(DiscussPost::getId)
                .ne(DiscussPost::getStatus, DiscussPostConstant.Status.BLOCKED)
                .eq(userId != 0, DiscussPost::getUserId, userId));
    }

    @Override
    public boolean addDiscussPost(DiscussPost discussPost) {
        // 校验空值
        if (Objects.isNull(discussPost)) {
            throw new IllegalArgumentException("DiscussPost is null!");
        }

        // 转移HTMl标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));

        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        // 保存讨论帖
        return this.save(discussPost);
    }

}
