package com.ywding1994.community.util;

/**
 * Redis工具类
 */
public class RedisKeyUtil {

    /**
     * 分隔符
     */
    private static final String SPLIT = ":";

    /**
     * 关键字前缀：实体的赞
     */
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    /**
     * 关键字前缀：用户的赞
     */
    private static final String PREFIX_USER_LIKE = "like:user";

    /**
     * 关键字前缀：被关注者
     */
    private static final String PREFIX_FOLLOWEE = "followee";

    /**
     * 关键字前缀：关注着
     */
    private static final String PREFIX_FOLLOWER = "follower";

    /**
     * 关键字前缀：验证码
     */
    private static final String PREFIX_KAPTCHA = "kaptcha";

    /**
     * 关键字前缀：登录凭证
     */
    private static final String PREFIX_TICKET = "ticket";

    /**
     * 关键字前缀：用户
     */
    private static final String PREFIX_USER = "user";

    /**
     * 关键字前缀：讨论帖
     */
    private static final String PREFIX_POST = "post";

    /**
     * 获取关键字：指定实体获得的赞
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 关键字：指定实体获得的赞
     */
    public static String getEntityLikeKey(int entityType, int entityId) {
        return String.join(SPLIT, PREFIX_ENTITY_LIKE, Integer.toString(entityType), Integer.toString(entityId));
    }

    /**
     * 获取关键字：指定用户获得的赞
     *
     * @param userId 用户id
     * @return 关键字：指定用户获得的赞
     */
    public static String getUserLikeKey(int userId) {
        return String.join(SPLIT, PREFIX_USER_LIKE, Integer.toString(userId));
    }

    /**
     * 获取关键字：指定用户关注的实体
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @return 关键字：指定用户关注的实体
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return String.join(SPLIT, PREFIX_FOLLOWEE, Integer.toString(userId), Integer.toString(entityType));
    }

    /**
     * 获取关键字：指定实体的关注者
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 关键字：指定实体的粉丝
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return String.join(SPLIT, PREFIX_FOLLOWER, Integer.toString(entityType), Integer.toString(entityId));
    }

    /**
     * 获取关键字：验证码
     *
     * @param owner 当前拥有者
     * @return 关键字：验证码
     */
    public static String getKaptchaKey(String owner) {
        return String.join(SPLIT, PREFIX_KAPTCHA, owner);
    }

    /**
     * 获取关键字：指定的登录凭证
     *
     * @param ticket 指定的登录凭证
     * @return 关键字：指定的登录凭证
     */
    public static String getTicketKey(String ticket) {
        return String.join(SPLIT, PREFIX_TICKET, ticket);
    }

    /**
     * 获取关键字：指定用户
     *
     * @param userId 用户id
     * @return 关键字：指定用户
     */
    public static String getUserKey(int userId) {
        return String.join(SPLIT, PREFIX_USER, Integer.toString(userId));
    }

    /**
     * 获取关键字：讨论帖分数
     *
     * @return 关键字：讨论帖分数
     */
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }

}
