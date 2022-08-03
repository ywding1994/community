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
    public List<Message> findConversations(int userId, int current, int limit);

    /**
     * 查询指定用户的会话数量
     * <p>
     * 所有查询均不包括状态为删除的消息。
     * </p>
     *
     * @param userId 用户id
     * @return 指定用户的会话数量
     */
    public int findConversationCount(int userId);

    /**
     * 分页查询指定会话包含的私信消息列表
     * <p>
     * 所有查询均不包括状态为删除的消息。
     * </p>
     *
     * @param conversationId 会话id
     * @param current        当前页码，从1开始
     * @param limit          每页消息数上限
     * @return 指定会话的分页消息列表，按id降序排列
     */
    public List<Message> findLetters(String conversationId, int current, int limit);

    /**
     * 查询指定会话包含的私信消息数量
     * <p>
     * 所有查询均不包括状态为删除的消息。
     * </p>
     *
     * @param conversationId 会话id
     * @return 指定会话的私信消息数量
     */
    public int findLetterCount(String conversationId);

    /**
     * 查询指定用户的指定会话包含的未读私信消息数量
     * <p>
     * 若不指定会话id，则查询指定用户的全部会话包含的未读私信消息数量。
     * </p>
     *
     * @param userId         用户id
     * @param conversationId 会话id
     * @return 未读私信消息数量
     */
    public int findLetterUnreadCount(int userId, String conversationId);

    /**
     * 新增消息
     *
     * @param message 消息实体
     */
    public void addMessage(Message message);

    /**
     * 批量更新指定消息的状态
     *
     * @param ids    消息id列表
     * @param status 待更新的消息状态
     */
    public void updateStatus(List<Integer> ids, int status);

    /**
     * 阅读消息
     * <p>
     * 将指定消息的状态更新为已读。
     * </p>
     *
     * @param ids 消息id列表
     */
    public void readMessages(List<Integer> ids);

    /**
     * 查询指定用户的指定主题下的最新通知
     * <p>
     * 只返回最新的一条通知，所有查询均不包括状态为删除的消息。
     * </p>
     *
     * @param userId 用户id
     * @param topic  指定主题
     * @return 指定用户的指定主题的最新通知
     */
    public Message findLatestNotice(int userId, String topic);

    /**
     * 查询指定用户的指定主题下的通知数量
     * <p>
     * 所有查询均不包括状态为删除的消息。
     * </p>
     *
     * @param userId 用户id
     * @param topic  指定主题
     * @return 指定用户的指定主题下的通知数量
     */
    public int findNoticeCount(int userId, String topic);

    /**
     * 查询指定用户的指定主题下的未读通知数量
     * <p>
     * 若不指定主题，则查询指定用户的全部未读通知数量。
     * </P>
     *
     * @param userId 用户id
     * @param topic  指定主题
     * @return 未读通知数量
     */
    public int findNoticeUnreadCount(int userId, String topic);

    /**
     * 分页查询指定用户的指定主题下的通知列表
     * <p>
     * 所有查询均不包括状态为删除的消息。
     * </p>
     *
     * @param userId  用户id
     * @param topic   指定主题
     * @param current 当前页码，从1开始
     * @param limit   每页通知数上限
     * @return 指定用户的指定主题下的通知列表，按创建时间降序排列
     */
    public List<Message> findNotices(int userId, String topic, int current, int limit);

}
