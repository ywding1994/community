package com.ywding1994.community.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.CommunityConstant;
import com.ywding1994.community.dao.UserMapper;
import com.ywding1994.community.entity.User;
import com.ywding1994.community.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, CommunityConstant {

}
