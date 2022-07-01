package com.ywding1994.community.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 分页信息类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页信息类")
public class Page {

    @ApiModelProperty(value = "当前页码")
    private int current = 1;

    @ApiModelProperty(value = "每页贴数上限")
    private int limit = 10;

    @ApiModelProperty(value = "发帖数量", hidden = true)
    private int rows;

    @ApiModelProperty(value = "查询路径（用于复用分页链接）", hidden = true)
    private String path;

    @Getter(AccessLevel.NONE)
    @ApiModelProperty(value = "总页数", hidden = true)
    private int pages;

    @Getter(AccessLevel.NONE)
    @ApiModelProperty(value = "起始页码", hidden = true)
    private int from;

    @Getter(AccessLevel.NONE)
    @ApiModelProperty(value = "结束页码", hidden = true)
    private int to;

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public int getPages() {
        return (int) Math.ceil(rows * 1.0 / limit);
    }

    /**
     * 获取页面显示时的起始页码
     *
     * @return 起始页码
     */
    public int getFrom() {
        int from = current - 2;
        return from >= 1 ? from : 1;
    }

    /**
     * 获取页面显示时的结束页码
     *
     * @return 结束页码
     */
    public int getTo() {
        int to = current + 2;
        int pages = getPages();
        return to <= pages ? to : pages;
    }

}
