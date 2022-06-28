package com.ywding1994.community;

import java.util.Random;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.UserService;
import com.ywding1994.community.util.CommunityUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testRegister() {
        log.info("---------- register test starting... ----------");
        String username = "mytest" + CommunityUtil.generateUUID().substring(0, 5);
        String password = "123456";
        String salt = CommunityUtil.generateUUID().substring(0, 5);
        String email = username + "@126.com";
        String activationCode = CommunityUtil.generateUUID();
        String headerUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        User user = User.builder().username(username).password(CommunityUtil.md5(password + salt)).salt(salt)
                .email(email).type(0).status(0).activationCode(activationCode).headerUrl(headerUrl).build();
        userService.save(user);
        log.info("---------- register test ended. ----------");
    }

}
