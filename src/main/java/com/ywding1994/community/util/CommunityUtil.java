package com.ywding1994.community.util;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSONObject;

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

    /**
     * 获取json字符串
     *
     * @param code 状态码
     * @param msg  信息
     * @param map  键值对
     * @return json字符串
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (MapUtils.isNotEmpty(map)) {
            for (String key : map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    /**
     * 获取json字符串
     *
     * @param code 状态码
     * @param msg  信息
     * @return
     */
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    /**
     * 获取json字符串
     *
     * @param code 状态码
     * @return json字符串
     */
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

}
