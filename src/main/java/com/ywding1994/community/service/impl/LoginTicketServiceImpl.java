package com.ywding1994.community.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.LoginTicketConsant;
import com.ywding1994.community.dao.LoginTicketMapper;
import com.ywding1994.community.entity.LoginTicket;
import com.ywding1994.community.service.LoginTicketService;

@Service
public class LoginTicketServiceImpl extends ServiceImpl<LoginTicketMapper, LoginTicket>
        implements LoginTicketService, LoginTicketConsant {

    @Override
    public void logout(String ticket) {
        this.update(new LambdaUpdateWrapper<>(LoginTicket.class).eq(LoginTicket::getTicket, ticket)
                .set(LoginTicket::getStatus, LoginTicketConsant.Status.INVALID));
    }

}
