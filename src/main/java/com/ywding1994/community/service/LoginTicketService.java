package com.ywding1994.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.LoginTicket;

public interface LoginTicketService extends IService<LoginTicket> {

    /**
     * 注销登录
     *
     * @param ticket 登陆凭证
     */
    public void logout(String ticket);

}
