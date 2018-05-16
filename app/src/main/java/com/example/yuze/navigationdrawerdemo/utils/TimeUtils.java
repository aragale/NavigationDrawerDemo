package com.example.yuze.navigationdrawerdemo.utils;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("MM月dd日HH时mm分");

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println(read("2018-05-12T13:37:51.411366").getMonthOfYear());
        System.out.println(format("2018-05-12T13:37:51.411366"));
    }

    /**
     * 将时间字符串转为LocalDateTime实例
     *
     * @param time 需要转换的时间字符串
     * @return LocalDateTime实例
     */
    public static LocalDateTime read(final String time) {
        return LocalDateTime.parse(time);
    }

    /**
     * 将输入的时间字符串格式化
     *
     * @param time 时间字符串
     * @return 格式化后的时间字符串
     */
    public static String format(final String time) {
        return DATE_TIME_FORMATTER.print(read(time));
    }
}
