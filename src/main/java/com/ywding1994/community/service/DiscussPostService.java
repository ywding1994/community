package com.ywding1994.community.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.DiscussPost;

public interface DiscussPostService extends IService<DiscussPost> {

    /**
     * 分页查询指定用户所发的帖子
     * <p>
     * 注：若userId = 0则分页查询所有用户所发的帖子，所有查询均不包括被拉黑的帖子。
     * </p>
     *
     * @param userId  用户ID
     * @param current 当前页码，从1开始
     * @param limit   每页贴数上限
     * @return 指定用户所发的帖子，按发帖类型、创建时间降序排列
     */
    public List<DiscussPost> findDiscussPosts(int userId, int current, int limit);

    /**
     * 查询指定用户的发帖数量
     * <p>
     * 注：若userId = 0则查询所有用户的发帖数量，所有查询均不包括被拉黑的帖子。
     * </p>
     *
     * @param userId 用户ID
     * @return 指定用户的发帖数量
     */
    public int findDiscussPostRows(int userId);

}
