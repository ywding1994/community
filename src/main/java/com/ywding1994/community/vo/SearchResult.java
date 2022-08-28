package com.ywding1994.community.vo;

import java.util.List;

import com.ywding1994.community.entity.DiscussPost;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Elasticsearch查询结果类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Elasticsearch查询结果类")
public class SearchResult {

    @ApiModelProperty(value = "当前页的查询结果列表")
    private List<DiscussPost> list;

    @ApiModelProperty(value = "查询结果总记录数")
    private long total;

}
