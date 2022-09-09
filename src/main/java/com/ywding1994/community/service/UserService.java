package com.ywding1994.community.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.LoginTicket;
import com.ywding1994.community.entity.User;

public interface UserService extends IService<User> {

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 查询到的用户实体
     */
    public User getUserById(int userId);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 查询到的用户实体
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

    /**
     * 修改密码
     *
     * @param userId   用户id
     * @param password 修改后的密码
     * @return 修改结果
     */
    public boolean updatePassword(int userId, String password);

    /**
     * 更新头像
     *
     * @param userId    用户id
     * @param headerUrl 头像地址
     * @return 更新结果
     */
    public boolean updateHeader(int userId, String headerUrl);

    /**
     * 根据登录凭证查询对应的实体
     *
     * @param ticket 登陆凭证
     * @return 登陆凭证对应的实体
     */
    public LoginTicket findLoginTicket(String ticket);

}
