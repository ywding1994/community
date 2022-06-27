package com.ywding1994.community.constant;

public interface DiscussPostConstant {

    /**
     * 发帖类型
     */
    public static class Type {

        /**
         * 普通贴
         */
        public static final int COMMON = 0;

        /**
         * 置顶帖
         */
        public static final int STICKY = 1;

    }

    /**
     * 帖子状态
     */
    public static class Status {

        /**
         * 正常
         */
        public static final int NORMAL = 0;

        /**
         * 加精
         */
        public static final int HIGHLIGHTED = 1;

        /**
         * 拉黑
         */
        public static final int BLOCKED = 2;

    }

}
