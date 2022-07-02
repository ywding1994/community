package com.ywding1994.community.service.impl;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.CommentConstant;
import com.ywding1994.community.dao.CommentMapper;
import com.ywding1994.community.entity.Comment;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.CommentService;
import com.ywding1994.community.service.DiscussPostService;
import com.ywding1994.community.util.SensitiveFilter;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private SensitiveFilter sensitiveFilter;

    @Resource
    private DiscussPostService discussPostService;

    @Override
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int current, int limit) {
        Page<Comment> page = new Page<>(current, limit, false);
        page = this.page(page,
                new LambdaQueryWrapper<>(Comment.class).eq(Comment::getEntityType, entityType)
                        .eq(Comment::getEntityId, entityId).eq(Comment::getStatus, CommentConstant.Status.NORMAL)
                        .orderByAsc(Comment::getCreateTime));
        return page.getRecords();
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        return (int) this.count(new LambdaQueryWrapper<>(Comment.class).eq(Comment::getEntityType, entityType)
                .eq(Comment::getEntityId, entityId).eq(Comment::getStatus, CommentConstant.Status.NORMAL));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void addComment(Comment comment) {
        // 校验空值
        if (Objects.isNull(comment)) {
            throw new IllegalArgumentException("Comment is null!");
        }

        // 添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        this.save(comment);

        // 更新讨论帖的评论数量
        if (comment.getEntityType() == CommentConstant.EntityType.ENTITY_TYPE_POST) {
            int count = this.findCommentCount(comment.getEntityType(), comment.getEntityId());
            discussPostService.update(new LambdaUpdateWrapper<>(DiscussPost.class)
                    .eq(DiscussPost::getId, comment.getEntityId()).set(DiscussPost::getCommentCount, count));
        }
    }

}
