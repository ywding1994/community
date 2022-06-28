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

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "user", autoResultMap = true)
@ApiModel(description = "用户实体类")
public class User {

    /**
     * id
     */
    @ApiModelProperty(value = "id", hidden = true)
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    /**
     * 盐值
     */
    @ApiModelProperty(value = "盐值", hidden = true)
    private String salt;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    /**
     * 用户权限
     */
    @ApiModelProperty(value = "用户权限", hidden = true)
    private Integer type;

    /**
     * 用户激活状态
     */
    @ApiModelProperty(value = "用户激活状态", hidden = true)
    private Integer status;

    /**
     * 激活码
     */
    @ApiModelProperty(value = "激活码", hidden = true)
    private String activationCode;

    /**
     * 头像URL地址
     */
    @ApiModelProperty(value = "头像URL地址", hidden = true)
    private String headerUrl;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
