package com.example.yuze.navigationdrawerdemo;

import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import org.junit.Test;

public class JsonUtilsTest {
    @Test
    public void readPositions() {
        LocationPoint read = JsonUtils.read("{\"time\":\"2018-05-02 05:20:31:524\",\"longitude\":1.0,\"latitude\":1.0}", LocationPoint.class);
        System.out.println(read.getTime());
    }
}
