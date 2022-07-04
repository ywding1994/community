package com.ywding1994.community;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class RedisTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testStrings() {
        log.info("---------- Redis Strings: test starting... ----------");
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey, 1);
        System.out.println("GET key: " + redisTemplate.opsForValue().get(redisKey));
        System.out.println("INCR key: " + redisTemplate.opsForValue().increment(redisKey));
        System.out.println("DECR key: " + redisTemplate.opsForValue().decrement(redisKey));
        log.info("---------- Redis Strings: test ended. ----------");
    }

    @Test
    public void testHashes() {
        log.info("---------- Redis Hashes: test starting... ----------");
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey, "id", 123456);
        redisTemplate.opsForHash().put(redisKey, "username", "ywding1994");
        System.out.println("HGET key field: " + redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println("HGET key field: " + redisTemplate.opsForHash().get(redisKey, "username"));
        log.info("---------- Redis Hashes: test ended. ----------");
    }

    @Test
    public void testLists() {
        log.info("---------- Redis Lists: test starting... ----------");
        String redisKey = "test:ids";
        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);
        System.out.println("LLEN key: " + redisTemplate.opsForList().size(redisKey));
        System.out.println("LINDEX key index: " + redisTemplate.opsForList().index(redisKey, 0));
        System.out.println("LRANGE key start stop: " + redisTemplate.opsForList().range(redisKey, 0, 2));
        System.out.println("LPOP key: " + redisTemplate.opsForList().leftPop(redisKey));
        System.out.println("LPOP key: " + redisTemplate.opsForList().leftPop(redisKey));
        System.out.println("LPOP key: " + redisTemplate.opsForList().leftPop(redisKey));
        log.info("---------- Redis Lists: test ended. ----------");
    }

    @Test
    public void testSets() {
        log.info("---------- Redis Sets: test starting... ----------");
        String redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey, "刘备", "关羽", "张飞", "赵云", "诸葛亮");
        System.out.println("SCARD key: " + redisTemplate.opsForSet().size(redisKey));
        System.out.println("SPOP key: " + redisTemplate.opsForSet().pop(redisKey));
        System.out.println("SMEMBERS key: " + redisTemplate.opsForSet().members(redisKey));
        log.info("---------- Redis Sets: test ended. ----------");
    }

    @Test
    public void testSortedSets() {
        log.info("---------- Redis SortedSets: test starting... ----------");
        String redisKey = "test:students";
        redisTemplate.opsForZSet().add(redisKey, "唐僧", 80);
        redisTemplate.opsForZSet().add(redisKey, "悟空", 90);
        redisTemplate.opsForZSet().add(redisKey, "八戒", 50);
        redisTemplate.opsForZSet().add(redisKey, "沙僧", 70);
        redisTemplate.opsForZSet().add(redisKey, "白龙马", 60);
        System.out.println("ZCARD key: " + redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println("ZSCORE key member: " + redisTemplate.opsForZSet().score(redisKey, "八戒"));
        System.out.println("ZREVRANK key member: " + redisTemplate.opsForZSet().reverseRank(redisKey, "八戒"));
        System.out.println(
                "ZREVRANGE key start stop [WITHSCORES]: " + redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));
        log.info("---------- Redis SortedSets: test ended. ----------");
    }

    @Test
    public void testKeys() {
        log.info("---------- Redis Keys: test starting... ----------");
        redisTemplate.delete("test:user");
        System.out.println("EXISTS key: " + redisTemplate.hasKey("test:user"));
        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);
        log.info("---------- Redis Keys: test ended. ----------");
    }

    @Test
    public void testBoundOperations() {
        log.info("---------- Redis BoundOperations: test starting... ----------");
        // 多次访问同一个key
        String redisKey = "test:count";
        BoundValueOperations<String, Object> boundValueOperations = redisTemplate.boundValueOps(redisKey);
        boundValueOperations.increment();
        boundValueOperations.increment();
        boundValueOperations.increment();
        boundValueOperations.increment();
        boundValueOperations.increment();
        System.out.println("GET key: " + boundValueOperations.get());
        log.info("---------- Redis BoundOperations: test ended. ----------");
    }

    @Test
    public void testTransactional() {
        log.info("---------- Redis Transactional: test starting... ----------");
        // Redis编程式事务
        Object obj = redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";
                // 开启事务
                operations.multi();
                operations.opsForSet().add(redisKey, "a1");
                operations.opsForSet().add(redisKey, "b1", "b2");
                operations.opsForSet().add(redisKey, "c1", "c2", "c3");
                System.out.println("SMEMBERS key: " + operations.opsForSet().members(redisKey));
                // 提交事务
                return operations.exec();
            }
        });
        // 在开启事务和结束事务之间进行的操作（添加、查询等），不会立即返回结果，而是等到结束事务后才返回结果，obj为这一系列操作的结果集。
        System.out.println("Transactional result: " + obj);
        log.info("---------- Redis Transactional: test ended. ----------");
    }

}
