package com.ywding1994.community.util;

import org.springframework.stereotype.Component;

import com.ywding1994.community.entity.User;

/**
 * 持有用户信息，用于代替session对象。
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
