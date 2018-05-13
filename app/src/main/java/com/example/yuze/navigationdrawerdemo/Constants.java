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

    public static final String FootPrints = "/api/foot-prints";

    /**
     * 百度定位时间格式
     */
    public static final DateTimeFormatter BAIDU_LOCATION_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 阿里云OSS端点
     */
    public static final String OSS_END_POINT = "oss-cn-shanghai.aliyuncs.com";

    /**
     * 阿里云OSS桶名称
     */
    public static final String OSS_BUCKET_NAME = "yztrip";

    /**
     * 阿里云OSS文件URL前缀
     */
    public static final String OSS_URL_PREFIX = "https://yztrip.oss-cn-shanghai.aliyuncs.com/";

    /**
     * Access ID
     */
    public static final String OSS_ACCESS_ID = "LTAI3Q9syMjEH3JH";

    /**
     * Access Secret Key
     */
    public static final String OSS_ACCESS_SECRET_KEY = "Ypx0vBAzYhLfhLIEaBcgqVckmnnBWi";
}
