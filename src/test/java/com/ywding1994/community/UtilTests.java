package com.ywding1994.community;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.ywding1994.community.util.CommunityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UtilTests {

    @Test
    public void testGetJSONString() {
        // test case 1
        log.info("---------- getJSONString: test case 1 starting... ----------");
        int code = 200;
        String msg = "ok";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "ywding1994");
        map.put("gender", "male");
        System.out.println(CommunityUtil.getJSONString(code, msg, map));
        log.info("---------- getJSONString: test case 1 ended. ----------");

        // test case 2
        log.info("---------- getJSONString: test case 2 starting... ----------");
        code = 404;
        System.out.println(CommunityUtil.getJSONString(code));
        log.info("---------- getJSONString: test case 2 ended. ----------");
    }

}
