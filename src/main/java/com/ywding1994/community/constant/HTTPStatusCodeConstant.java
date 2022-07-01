package com.ywding1994.community.constant;

/**
 * 常用HTTP状态码
 */
public interface HTTPStatusCodeConstant {

    /**
     * 请求成功
     */
    public static final int OK = 200;

    /**
     * 客户端请求存在语法错误，服务器无法理解
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 服务器理解客户端的请求，但是拒绝执行此请求
     */
    public static final int FORBIDDEN = 403;

    /**
     * 服务器无法根据客户端的请求找到资源
     */
    public static final int NOT_FOUND = 404;

    /**
     * 内部服务器错误，无法完成请求
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

}
