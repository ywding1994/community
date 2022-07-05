package com.ywding1994.community.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.dao.MessageMapper;
import com.ywding1994.community.entity.Message;
import com.ywding1994.community.service.MessageService;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> selectConversations(int userId, int current, int limit) {
        Page<Message> page = new Page<>(current, limit, false);
        List<Integer> conversationIds = this.getBaseMapper().selectConversationIds(userId);
        page = this.page(page, new LambdaQueryWrapper<>(Message.class).in(Message::getId, conversationIds)
                .orderByDesc(Message::getId));
        return page.getRecords();
    }

}
