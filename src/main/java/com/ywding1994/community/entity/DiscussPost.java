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
 * 讨论帖实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "discuss_post", autoResultMap = true)
public class DiscussPost {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 发帖类型
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer type;

    /**
     * 帖子状态
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 评论数量
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer commentCount;

    /**
     * 评分
     */
    @TableField(fill = FieldFill.INSERT)
    private Double score;

}
