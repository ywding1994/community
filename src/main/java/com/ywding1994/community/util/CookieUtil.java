package com.ywding1994.community.util;

import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Cookie工具类
 */

public class CookieUtil {

    public static String getValue(HttpServletRequest request, String name) {
        if (Objects.isNull(request) || Objects.isNull(name)) {
            throw new IllegalArgumentException("Request or name is null!");
        }

        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
