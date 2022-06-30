package com.ywding1994.community.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.User;

public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 查询到的用户
     */
    public User getUserByUsername(String username);

    /**
     * 注册账号
     *
     * @param user 待注册用户
     * @return 注册结果
     */
    public Map<String, Object> register(User user);

    /**
     * 激活账号
     *
     * @param userId         用户id
     * @param activationCode 激活码
     * @return 激活结果（成功：0，重复：1，失败：2）
     */
    public int activation(int userId, String activationCode);

    /**
     * 用户登录
     *
     * @param username       用户名
     * @param password       密码
     * @param expiredSeconds 登录凭证过期时间
     * @return 登录结果
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds);

    /**
     * 注销登录
     *
     * @param ticket 登录凭证
     */
    public void logout(String ticket);

}
