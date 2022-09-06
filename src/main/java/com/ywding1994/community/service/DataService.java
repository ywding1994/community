package com.ywding1994.community.service;

import java.util.Date;

public interface DataService {

    /**
     * 将指定的ip计入UV
     *
     * @param ip 指定ip
     */
    public void recordUV(String ip);

    /**
     * 统计指定日期范围内的UV
     *
     * @param start 起始日期
     * @param end   终止日期
     * @return 统计结果
     */
    public long calculateUV(Date start, Date end);

    /**
     * 将指定用户计入DAU
     *
     * @param userId 用户id
     */
    public void recordDAU(int userId);

    /**
     * 统计指定日期范围内的DAU
     *
     * @param start 起始日期
     * @param end   终止日期
     * @return 统计结果
     */
    public long calculateDAU(Date start, Date end);

}
