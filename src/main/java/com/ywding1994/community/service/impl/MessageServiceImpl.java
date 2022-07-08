package com.ywding1994.community.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywding1994.community.constant.MessageConstant;
import com.ywding1994.community.dao.MessageMapper;
import com.ywding1994.community.entity.Message;
import com.ywding1994.community.service.MessageService;
import com.ywding1994.community.util.SensitiveFilter;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Message> findConversations(int userId, int current, int limit) {
        Page<Message> page = new Page<>(current, limit, false);
        List<Integer> conversationIds = this.getBaseMapper().selectConversationIds(userId);
        page = this.page(page, new LambdaQueryWrapper<>(Message.class).in(Message::getId, conversationIds)
                .orderByDesc(Message::getId));
        return page.getRecords();
    }

    @Override
    public int findConversationCount(int userId) {
        return this.getBaseMapper().selectConversationIds(userId).size();
    }

    @Override
    public List<Message> findLetters(String conversationId, int current, int limit) {
        Page<Message> page = new Page<>(current, limit, false);
        page = this.page(page,
                new LambdaQueryWrapper<>(Message.class).ne(Message::getStatus, MessageConstant.Status.DELETED)
                        .ne(Message::getFromId, MessageConstant.FromId.RESERVED)
                        .eq(Message::getConversationId, conversationId).orderByDesc(Message::getId));
        return page.getRecords();
    }

    @Override
    public int findLetterCount(String conversationId) {
        return (int) this
                .count(new LambdaQueryWrapper<>(Message.class).ne(Message::getStatus, MessageConstant.Status.DELETED)
                        .ne(Message::getFromId, MessageConstant.FromId.RESERVED)
                        .eq(Message::getConversationId, conversationId));
    }

    @Override
    public int findLetterUnreadCount(int userId, String conversationId) {
        return (int) this
                .count(new LambdaQueryWrapper<>(Message.class).ne(Message::getFromId, MessageConstant.FromId.RESERVED)
                        .eq(Message::getStatus, MessageConstant.Status.UNREAD).eq(Message::getToId, userId)
                        .eq(StringUtils.isNotBlank(conversationId), Message::getConversationId, conversationId));
    }

    @Override
    public void addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        this.save(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(List<Integer> ids, int status) {
        for (Integer id : ids) {
            this.update(
                    new LambdaUpdateWrapper<>(Message.class).eq(Message::getId, id).set(Message::getStatus, status));
        }
    }

    @Override
    public void readMessages(List<Integer> ids) {
        updateStatus(ids, MessageConstant.Status.READ);
    }

}
