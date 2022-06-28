package com.ywding1994.community.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * community项目相关的工具类
 */
public class CommunityUtil {

    /**
     * 通过UUID生成随机字符串
     *
     * @return 长度为32的随机字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * md5加密
     *
     * @param key 待加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}