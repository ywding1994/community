package com.ywding1994.community.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.Comment;

public interface CommentService extends IService<Comment> {

    /**
     * 分页查询指定实体的评论
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @param current    当前页码，从1开始
     * @param limit      每页评论上限
     * @return 指定实体的评论，按创建时间升序排列
     */
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int current, int limit);

    /**
     * 查询指定实体的评论数量
     * <p>
     * 注：仅查询状态为正常的评论.
     * </p>
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 指定实体的评论数量
     */
    public int findCommentCount(int entityType, int entityId);

    /**
     * 添加评论
     *
     * @param comment 待添加的评论
     */
    public void addComment(Comment comment);

}
