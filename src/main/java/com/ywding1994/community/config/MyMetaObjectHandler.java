package com.ywding1994.community.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ywding1994.community.constant.FillConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * MyBatis-Plus自动填充处理器
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ...");
        this.strictInsertFill(metaObject, "type", Integer.class, FillConstant.TYPE);
        this.strictInsertFill(metaObject, "status", Integer.class, FillConstant.STATUS);
        this.strictInsertFill(metaObject, "commentCount", Integer.class, FillConstant.COMMENT_COUNT);
        this.strictInsertFill(metaObject, "score", Double.class, FillConstant.SCORE);
        this.strictInsertFill(metaObject, "createTime", Date.class, FillConstant.getcreateTime());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    }

}
