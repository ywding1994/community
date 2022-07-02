package com.ywding1994.community.constant;

public interface CommentConstant {

    /**
     * 实体类型
     */
    public static class EntityType {

        /**
         * 实体类型: 帖子
         */
        public static final int ENTITY_TYPE_POST = 1;

        /**
         * 实体类型: 评论
         */
        public static final int ENTITY_TYPE_COMMENT = 2;

        /**
         * 实体类型: 用户
         */
        public static final int ENTITY_TYPE_USER = 3;

    }

    /**
     * 评论状态
     */
    public static class Status {

        /**
         * 正常
         */
        public static final int NORMAL = 0;

        /**
         * 拉黑
         */
        public static final int BLOCKED = 1;

    }

}
