package com.ywding1994.community.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.DiscussPostConstant;
import com.ywding1994.community.dao.DiscussPostMapper;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.DiscussPostService;

@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost>
        implements DiscussPostService, DiscussPostConstant {

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

}
