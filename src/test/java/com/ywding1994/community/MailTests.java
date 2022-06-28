package com.ywding1994.community;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ywding1994.community.util.MailCilent;

@SpringBootTest
public class MailTests {

    @Resource
    private MailCilent mailCilent;

    @Resource
    private TemplateEngine templateEngine;

    @Test
    public void testSetMail() {
        String to = "ywding1994@126.com";
        Context context = new Context();
        context.setVariable("username", to);
        String content = templateEngine.process("/mail/demo", context);

        mailCilent.sendMail(to, "邮件发送功能测试", content);
    }
}
