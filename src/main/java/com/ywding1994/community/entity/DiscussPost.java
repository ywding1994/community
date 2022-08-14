package com.ywding1994.community.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "discusspost", shards = 5, replicas = 1)
@TableName(value = "discuss_post", autoResultMap = true)
public class DiscussPost {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 用户id
     */
    @Field(type = FieldType.Integer)
    private Integer userId;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    /**
     * 发帖类型
     */
    @Field(type = FieldType.Integer)
    @TableField(fill = FieldFill.INSERT)
    private Integer type;

    /**
     * 帖子状态
     */
    @Field(type = FieldType.Integer)
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Integer)
    @TableField(fill = FieldFill.INSERT)
    private Integer commentCount;

    /**
     * 评分
     */
    @Field(type = FieldType.Double)
    @TableField(fill = FieldFill.INSERT)
    private Double score;

}
