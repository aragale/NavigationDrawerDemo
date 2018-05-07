package com.example.yuze.navigationdrawerdemo;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class ConstantsTest {
    @Test
    public void formatterTest() {
        String text = "2018-05-04 14:13:03";
        LocalDateTime localDateTime = LocalDateTime.parse(text, Constants.BAIDU_LOCATION_TIME_FORMATTER);
        Assert.assertEquals(text, Constants.BAIDU_LOCATION_TIME_FORMATTER.format(localDateTime));
    }
}
