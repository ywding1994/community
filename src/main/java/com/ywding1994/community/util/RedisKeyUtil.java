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
     * 关键字前缀：UV
     * <p>
     * UV：Unique Vistor，独立访客，需通过用户ip排重统计数据，且每次访问都需要进行统计。
     * </p>
     */
    private static final String PREFIX_UV = "uv";

    /**
     * 关键字前缀：DAU
     * <p>
     * DAU：Daily Active User，日活跃用户，需通过用户ip排重统计数据，访问过一次则认为其活跃。
     * </p>
     */
    private static final String PREFIX_DAU = "dau";

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

    /**
     * 获取关键字：单日UV
     *
     * @param date 日期
     * @return 关键字：单日UV
     */
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    /**
     * 获取关键字：区间UV
     *
     * @param startDate 起始日期
     * @param endDate   终止日期
     * @return 关键字：区间UV
     */
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    /**
     * 获取关键字：单日DAU
     *
     * @param date 日期
     * @return 关键字：单日DAU
     */
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    /**
     * 获取关键字：区间DAU
     *
     * @param startDate 起始日期
     * @param endDAte   终止日期
     * @return 关键字：区间DAU
     */
    public static String getDAUKey(String startDate, String endDAte) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDAte;
    }

}
