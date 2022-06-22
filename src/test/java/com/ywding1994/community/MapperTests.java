package com.ywding1994.community;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywding1994.community.dao.UserMapper;
import com.ywding1994.community.entity.User;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MapperTests {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectAll() {
        log.info("----- selectAll method test ------");
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectPage() {
        log.info("----- selectPage method test ------");
        Page<User> page = new Page<>(5, 5);
        page = userMapper.selectPage(page, null);
        List<User> userList = page.getRecords();
        userList.forEach(System.out::println);
    }

}
