package com.ywding1994.community;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ywding1994.community.entity.DiscussPost;
import com.ywding1994.community.service.DiscussPostService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class DiscussPostServiceTests {

    @Resource
    private DiscussPostService discussPostService;

    @Test
    public void testFindDiscussPosts() {

        // test case 1
        log.info("---------- findDiscussPosts: test case 1 starting... ----------");
        int userId = 101;
        int current = 1;
        int limit = 5;
        List<DiscussPost> discussPosts = null;
        discussPosts = discussPostService.findDiscussPosts(userId, current, limit);
        discussPosts.forEach(System.out::println);
        log.info("---------- findDiscussPosts: test case 1 ended. ----------");

        // test case 2
        log.info("---------- findDiscussPosts: test case 2 starting... ----------");
        userId = 0;
        current = 5;
        limit = 10;
        discussPosts = discussPostService.findDiscussPosts(userId, current, limit);
        discussPosts.forEach(System.out::println);
        log.info("---------- findDiscussPosts: test case 2 ended. ----------");

    }

}
