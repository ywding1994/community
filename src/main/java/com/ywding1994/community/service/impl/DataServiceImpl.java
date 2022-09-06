package com.ywding1994.community.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ywding1994.community.service.DataService;
import com.ywding1994.community.util.RedisKeyUtil;

@Service
public class DataServiceImpl implements DataService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void recordUV(String ip) {
        String redisKey = RedisKeyUtil.getUVKey(simpleDateFormat.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey, ip);
    }

    @Override
    public long calculateUV(Date start, Date end) {
        // 参数校验
        if (Objects.isNull(start) || Objects.isNull(end)) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("起始日期不能晚于终止日期！");
        }

        // 整理该日期范围内的Key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getUVKey(simpleDateFormat.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE, 1);
        }

        // 合并该日期范围内的数据并返回统计结果
        String redisKey = RedisKeyUtil.getUVKey(simpleDateFormat.format(start), simpleDateFormat.format(end));
        redisTemplate.opsForHyperLogLog().union(redisKey, (String[]) keyList.toArray(new String[0]));
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    @Override
    public void recordDAU(int userId) {
        // 以用户id作为Bitmap的偏移量
        String redisKey = RedisKeyUtil.getDAUKey(simpleDateFormat.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey, userId, true);
    }

    @Override
    public long calculateDAU(Date start, Date end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("起始日期不能晚于终止日期！");
        }

        // 整理该日期范围内的Key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getDAUKey(simpleDateFormat.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }

        // 通过OR运算统计该日期范围内的数据并返回统计结果
        return (long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(simpleDateFormat.format(start), simpleDateFormat.format(end));
                connection.bitOp(RedisStringCommands.BitOperation.OR, redisKey.getBytes(),
                        keyList.toArray(new byte[0][0]));
                return connection.bitCount(redisKey.getBytes());
            }
        });
    }

}
