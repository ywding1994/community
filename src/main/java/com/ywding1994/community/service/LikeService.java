package com.ywding1994.community.service;

public interface LikeService {

    /**
     * 点赞
     *
     * @param userId       用户id
     * @param entityType   实体类型
     * @param entityId     实体id
     * @param entityUserId 实体对应的用户id
     */
    public void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * 查询指定实体的点赞数
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 指定实体的点赞数
     */
    public long findEntityLikeCount(int entityType, int entityId);

    /**
     * 查询指定用户对指定实体的点赞状态
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 点赞状态（已点赞：1，未点赞：0）
     */
    public int findEntityLikeStatus(int userId, int entityType, int entityId);

    /**
     * 查询指定用户获得的赞数
     *
     * @param userId 用户id
     * @return 指定用户获得的赞数
     */
    public int findUserLikeCount(int userId);

}
