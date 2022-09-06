package com.ywding1994.community.actuator;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import com.ywding1994.community.constant.HTTPStatusCodeConstant;
import com.ywding1994.community.util.CommunityUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Endpoint(id = "database")
@Slf4j
public class DatabaseEndPoint {

    @Resource
    private DataSource dataSource;

    @ReadOperation
    public String checkConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return CommunityUtil.getJSONString(HTTPStatusCodeConstant.OK, "获取连接成功！");
        } catch (SQLException e) {
            log.error("获取连接失败：" + e.getMessage());
            return CommunityUtil.getJSONString(HTTPStatusCodeConstant.INTERNAL_SERVER_ERROR, "获取连接失败！");
        }
    }

}
