package com.ywding1994.community.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.constant.DiscussPostConstant;
import com.ywding1994.community.entity.DiscussPost;

public interface DiscussPostService extends IService<DiscussPost> {

    /**
     * 分页查询指定用户所发的讨论帖
     * <p>
     * 注：若userId = 0则分页查询所有用户所发的讨论帖，所有查询均不包括被拉黑的讨论帖。
     * </p>
     *
     * @param userId    用户id
     * @param current   当前页码，从1开始
     * @param limit     每页贴数上限
     * @param orderMode 排序模式，0为默认排列（按发帖类型、创建时间降序排列），1为按热度排列（按发帖类型、讨论帖分数、创建时间降序排列）
     * @return 指定用户所发的讨论帖
     */
    public List<DiscussPost> findDiscussPosts(int userId, int current, int limit, int orderMode);

    /**
     * 查询指定用户的发帖数量
     * <p>
     * 注：若userId = 0则查询所有用户的发帖数量，所有查询均不包括被拉黑的讨论帖。
     * </p>
     *
     * @param userId 用户id
     * @return 指定用户的发帖数量
     */
    public int findDiscussPostRows(int userId);

    /**
     * 发布讨论帖
     *
     * @param discussPost 待发布的讨论帖
     * @return 发布结果
     */
    public boolean addDiscussPost(DiscussPost discussPost);

    /**
     * 更新指定讨论帖的类型
     *
     * @param id   讨论帖id
     * @param type 讨论帖类型，参见{@link DiscussPostConstant.Type}
     * @return 更新结果
     */
    public boolean updateType(int id, int type);

    /**
     * 更新指定讨论帖的状态
     *
     * @param id     讨论帖id
     * @param status 讨论帖状态，参见{@link DiscussPostConstant.Status}
     * @return 更新结果
     */
    public boolean updateStatus(int id, int status);

    /**
     * 更新指定讨论帖的分数
     *
     * @param id    讨论帖id
     * @param score 待更新的分数
     * @return 更新结果
     */
    public boolean updateScore(int id, double score);

}
