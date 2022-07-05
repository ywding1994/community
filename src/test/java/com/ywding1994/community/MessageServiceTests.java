package com.ywding1994.community;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ywding1994.community.entity.Message;
import com.ywding1994.community.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MessageServiceTests {

    @Resource
    private MessageService messageService;

    @Test
    public void testSelectConversations() {
        log.info("---------- selectConversations: test starting... ----------");
        int userId = 115;
        int current = 1;
        int limit = 10;
        List<Message> messages = messageService.selectConversations(userId, current, limit);
        messages.forEach(System.out::println);
        log.info("---------- selectConversations: test ended. ----------");
    }

}
