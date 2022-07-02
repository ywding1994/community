package com.ywding1994.community.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "comment", autoResultMap = true)
public class Comment {

    /**
     * id
     */
    private Integer id;

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
     * 目标id
     */
    private Integer targetId;

    /**
     * 内容
     */
    private String content;

    /**
     * 评论状态
     */
    private int status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
