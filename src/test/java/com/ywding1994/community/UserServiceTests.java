package com.ywding1994.community;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.CommunityUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UserServiceTests {

    @Resource
    private UserService userService;

    @Test
    public void testRegister() {
        log.info("---------- register: test starting... ----------");
        String username = "mytest" + CommunityUtil.generateUUID().substring(0, 5);
        String password = "123456";
        String email = username + "@126.com";
        User user = User.builder().username(username).password(password).email(email).build();
        Map<String, Object> map = userService.register(user);
        map.forEach((k, v) -> System.out.println(k + ": " + v));
        log.info("---------- register: test ended. ----------");
    }

}
