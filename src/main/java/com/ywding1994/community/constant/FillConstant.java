package com.ywding1994.community.constant;

import java.util.Date;

/**
 * MyBatis-Plus自动填充时的默认数据
 */
public interface FillConstant {

    /**
     * 默认类型
     */
    public static final Integer TYPE = 0;

    /**
     * 默认状态
     */
    public static final Integer STATUS = 0;

    /**
     * 默认评论数
     */
    public static final Integer COMMENT_COUNT = 0;

    /**
     * 默认评分
     */
    public static final Double SCORE = 0.0;

    /**
     * 目标id
     */
    public static final Integer TARGET_ID = 0;

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    public static Date getcreateTime() {
        return new Date();
    }

}
