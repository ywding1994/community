package com.ywding1994.community.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.LoginTicketConstant;
import com.ywding1994.community.dao.LoginTicketMapper;
import com.ywding1994.community.entity.LoginTicket;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.LoginTicketService;
import com.ywding1994.community.util.CommunityUtil;

@Service
public class LoginTicketServiceImpl extends ServiceImpl<LoginTicketMapper, LoginTicket> implements LoginTicketService {

        @Override
        public LoginTicket generateLoginTicket(User user, int expiredSeconds) {
                LoginTicket loginTicket = LoginTicket.builder().userId(user.getId())
                                .ticket(CommunityUtil.generateUUID())
                                .status(LoginTicketConstant.Status.VALID)
                                .expired(new Date(System.currentTimeMillis() + expiredSeconds * 1000)).build();
                this.save(loginTicket);
                return loginTicket;
        }

        @Override
        public void updateLoginTicket(String ticket, int status) {
                Assert.isTrue(status == LoginTicketConstant.Status.VALID
                                || status == LoginTicketConstant.Status.INVALID,
                                "不合法的登录状态！");
                this.update(new LambdaUpdateWrapper<>(LoginTicket.class).eq(LoginTicket::getTicket, ticket)
                                .set(LoginTicket::getStatus, status));
        }

        @Override
        public LoginTicket findLoginTicket(String ticket) {
                return this.getOne(new LambdaQueryWrapper<>(LoginTicket.class).eq(LoginTicket::getTicket, ticket));
        }

}
