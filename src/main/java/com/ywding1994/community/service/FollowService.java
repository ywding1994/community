package com.ywding1994.community.service;

import java.util.List;
import java.util.Map;

public interface FollowService {

    /**
     * 关注
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     */
    public void follow(int userId, int entityType, int entityId);

    /**
     * 取关
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     */
    public void unfollow(int userId, int entityType, int entityId);

    /**
     * 查询指定用户关注的实体的数量
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @return 指定用户关注的实体的数量
     */
    public long findFolloweeCount(int userId, int entityType);

    /**
     * 查询指定实体的关注者的数量
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 指定实体的关注者的数量
     */
    public long findFollowerCount(int entityType, int entityId);

    /**
     * 查询指定用户是否已关注指定实体
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 关注状态（已关注：true，未关注：false）
     */
    public boolean hasFollowed(int userId, int entityType, int entityId);

    /**
     * 查询指定用户关注的用户列表
     *
     * @param userId  用户id
     * @param current 当前页码，从1开始
     * @param limit   每页关注信息数上限
     * @return 指定用户关注的用户列表
     */
    public List<Map<String, Object>> findFollowees(int userId, int current, int limit);

    /**
     * 查询关注指定用户的用户列表
     *
     * @param userId  用户id
     * @param current 当前页码，从1开始
     * @param limit   每页关注信息数上限
     * @return 关注指定用户的用户列表
     */
    public List<Map<String, Object>> findFollowers(int userId, int current, int limit);

}
