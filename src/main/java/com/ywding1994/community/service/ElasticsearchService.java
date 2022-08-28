package com.ywding1994.community.service;

import java.io.IOException;

import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.vo.SearchResult;

public interface ElasticsearchService {

    /**
     * 将指定的讨论帖保存到Elasticsearch服务器
     *
     * @param discussPost 待保存的讨论帖实体
     */
    public void saveDiscussPost(DiscussPost discussPost);

    /**
     * 从Elasticsearch服务器中删除指定的讨论帖
     *
     * @param id 讨论帖id
     */
    public void deleteDiscussPost(int id);

    /**
     * 从Elasticsearch服务器中查询讨论帖
     *
     * @param keyword 查询关键词
     * @param offset  查询时的偏移量
     * @param limit   每页贴数上限
     * @return Elasticsearch查询结果类
     * @throws IOException
     */
    public SearchResult searchDiscussPost(String keyword, int offset, int limit) throws IOException;

}
