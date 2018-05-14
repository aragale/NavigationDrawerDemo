package com.example.yuze.navigationdrawerdemo.utils;

import java.time.LocalDateTime;

public class TimeUtils {
    public static void main(String[] args) {
        System.out.println(read("2018-05-12T13:37:51.411366").getMonth());
    }

    public static LocalDateTime read(final String time) {
        return LocalDateTime.parse(time);
    }
}
