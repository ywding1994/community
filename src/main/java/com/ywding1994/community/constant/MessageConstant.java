package com.ywding1994.community.constant;

public interface MessageConstant {

    /**
     * 发送方id
     */
    public static class FromId {

        /**
         * 保留字
         */
        public static final int RESERVED = 1;

    }

    /**
     * 消息状态
     */
    public static class Status {

        /**
         * 未读
         */
        public static final int UNREAD = 0;

        /**
         * 已读
         */
        public static final int READ = 1;

        /**
         * 删除
         */
        public static final int DELETED = 2;

    }

}
