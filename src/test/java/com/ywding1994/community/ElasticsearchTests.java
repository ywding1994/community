package com.ywding1994.community;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.ywding1994.community.dao.elasticsearch.DiscussPostRepository;
import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.DiscussPostService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ElasticsearchTests {

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private DiscussPostRepository discussPostRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testExist() {
        log.info("---------- Elasticsearch: test isExist starting... ----------");
        System.out.println(discussPostRepository.existsById(109));
        log.info("---------- Elasticsearch: test isExist ended. ----------");
    }

    @Test
    public void testInsert() {
        log.info("---------- Elasticsearch: test insert starting... ----------");
        discussPostRepository.save(discussPostService.getById(241));
        discussPostRepository.save(discussPostService.getById(242));
        discussPostRepository.save(discussPostService.getById(243));
        log.info("---------- Elasticsearch: test insert ended. ----------");
    }

    @Test
    public void testInsertList() {
        log.info("---------- Elasticsearch: test insertList starting... ----------");
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(101, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(102, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(103, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(111, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(112, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(131, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(132, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(133, 1, 100));
        discussPostRepository.saveAll(discussPostService.findDiscussPosts(134, 1, 100));
        log.info("---------- Elasticsearch: test insertList ended. ----------");
    }

    @Test
    public void testUpdate() {
        log.info("---------- Elasticsearch: test update starting... ----------");
        DiscussPost discussPost = discussPostService.getById(231);
        discussPost.setContent("Hello World!");
        discussPostRepository.save(discussPost);
        log.info("---------- Elasticsearch: test update ended. ----------");
    }

    @Test
    public void testDelete() {
        log.info("---------- Elasticsearch: test delete starting... ----------");
        discussPostRepository.deleteAll();
        log.info("---------- Elasticsearch: test delete ended. ----------");
    }

    @Test
    public void testSearchByTemplate() {
        log.info("---------- Elasticsearch: test search by template starting... ----------");
        // 构造查询条件
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>"))
                .build();

        // 获取查询结果
        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(query, DiscussPost.class);
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();

        // 对查询结果进行处理并输出
        List<DiscussPost> discussPosts = new ArrayList<>();
        for (SearchHit<DiscussPost> searchHit : searchHits) {
            // 将高亮内容填充至content
            Map<String, List<String>> highLightFields = searchHit.getHighlightFields();
            searchHit.getContent()
                    .setTitle(Objects.isNull(highLightFields.get("title")) ? searchHit.getContent().getTitle()
                            : highLightFields.get("title").get(0));
            searchHit.getContent()
                    .setContent(Objects.isNull(highLightFields.get("content")) ? searchHit.getContent().getContent()
                            : highLightFields.get("content").get(0));

            // 将填充后的content放入实体类中
            discussPosts.add(searchHit.getContent());
        }
        discussPosts.forEach(System.out::println);
        log.info("---------- Elasticsearch: test search by template ended. ----------");
    }

}
