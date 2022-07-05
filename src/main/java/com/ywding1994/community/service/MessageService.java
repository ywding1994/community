package com.ywding1994.community.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywding1994.community.entity.Message;

public interface MessageService extends IService<Message> {

    /**
     * 分页查询指定用户的会话列表
     * <p>
     * 每个会话只返回最新的一条私信消息，所有查询均不包括状态为删除的消息。
     * </p>
     *
     * @param userId  用户id
     * @param current 当前页码，从1开始
     * @param limit   每页消息数上限
     * @return 指定用户的会话列表，按id降序排列
     */
    public List<Message> selectConversations(int userId, int current, int limit);

}
