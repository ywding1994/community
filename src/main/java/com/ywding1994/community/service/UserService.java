package com.ywding1994.community.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.User;

public interface UserService extends IService<User> {

    /**
     * 注册账号
     *
     * @param user 待注册用户
     * @return 注册结果
     */
    public Map<String, Object> register(User user);

}
