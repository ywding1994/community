package com.ywding1994.community.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywding1994.community.entity.Comment;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
