package com.ywding1994.community.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "message", autoResultMap = true)
public class Message {

    /**
     * id
     */
    private Integer id;

    /**
     * 发件人id
     */
    private Integer fromId;

    /**
     * 收件人id
     */
    private Integer toId;

    /**
     * 会话id
     */
    private String conversationId;

    /**
     * 私信消息内容
     */
    private String content;

    /**
     * 消息状态
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
