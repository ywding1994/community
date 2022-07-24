package com.ywding1994.community.event;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    /**
     * 主题
     */
    private String topic;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 实体类型
     */
    private Integer entityType;

    /**
     * 实体id
     */
    private Integer entityId;

    /**
     * 实体对应的用户id
     */
    private Integer entityUserId;

    /**
     * 数据
     */
    private Map<String, Object> data;

}
