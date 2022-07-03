package com.ywding1994.community.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "comment", autoResultMap = true)
@ApiModel(description = "评论实体类")
public class Comment {

    /**
     * id
     */
    @ApiModelProperty(value = "id", hidden = true)
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", hidden = true)
    private Integer userId;

    /**
     * 实体类型
     */
    @ApiModelProperty(value = "实体类型", hidden = true)
    private Integer entityType;

    /**
     * 实体id
     */
    @ApiModelProperty(value = "实体id", hidden = true)
    private Integer entityId;

    /**
     * 目标id
     */
    @ApiModelProperty(value = "目标id", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer targetId;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    private String content;

    /**
     * 评论状态
     */
    @ApiModelProperty(value = "评论状态", hidden = true)
    private int status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
