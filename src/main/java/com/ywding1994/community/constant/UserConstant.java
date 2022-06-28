package com.ywding1994.community.constant;

public interface UserConstant {

    /**
     * 用户权限
     */
    public static class Type {

        /**
         * 普通用户
         */
        public static final int USER = 0;

        /**
         * 管理员
         */
        public static final int ADMIN = 1;

        /**
         * 版主
         */
        public static final int MODERATOR = 2;

    }

    /**
     * 用户激活状态
     */
    public static class Status {

        /**
         * 未激活
         */
        public static final int UNACTIVATED = 0;

        /**
         * 已激活
         */
        public static final int ACTIVATED = 1;

    }

}
