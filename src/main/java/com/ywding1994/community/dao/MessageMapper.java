package com.ywding1994.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywding1994.community.entity.Message;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 查询与指定用户相关的会话id
     * <p>
     * 每个会话id以最新的一条私信消息id表示，所有查询均不包括状态为删除的消息。
     * </P>
     *
     * @param userId 用户id
     * @return 与指定用户相关的会话id
     */
    @Select("select max(id) as id from message where status != 2 and from_id != 1"
            + " and (from_id = #{userId} or to_id = #{userId}) group by conversation_id")
    public List<Integer> selectConversationIds(int userId);

}
