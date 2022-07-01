package com.ywding1994.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.LoginTicket;
import com.ywding1994.community.entity.User;

public interface LoginTicketService extends IService<LoginTicket> {

    /**
     * 生成用户的登录凭证
     *
     * @param user           用户
     * @param expiredSeconds 过期时间
     * @return 登录凭证，状态为有效
     */
    public LoginTicket generateLoginTicket(User user, int expiredSeconds);

    /**
     * 更新登录凭证的状态
     *
     * @param ticket 登陆凭证
     * @param status 登录状态，只能为有效（0）或无效（1）
     */
    public void updateLoginTicket(String ticket, int status);

    /**
     * 根据登录凭证查询对应的实体
     *
     * @param ticket 登陆凭证
     * @return 登陆凭证对应的实体
     */
    public LoginTicket findLoginTicket(String ticket);

}
