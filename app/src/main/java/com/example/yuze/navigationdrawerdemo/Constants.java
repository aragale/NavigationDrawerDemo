package com.example.yuze.navigationdrawerdemo;

import java.time.format.DateTimeFormatter;

/**
 * chang liang
 */
public class Constants {
    public static final String HOST = "http://47.93.221.223:8002";

    public static final String USERS = "/api/users";

    public static final String SESSIONS = "/api/sessions";

    public static final String TRACES = "/api/traces";

    /**
     * 百度定位时间格式
     */
    public static final DateTimeFormatter BAIDU_LOCATION_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
