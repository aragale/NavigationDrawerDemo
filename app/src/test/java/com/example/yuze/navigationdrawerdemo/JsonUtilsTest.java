package com.example.yuze.navigationdrawerdemo;

import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;
import com.example.yuze.navigationdrawerdemo.dto.LocationPointsResponse;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import org.junit.Test;

import java.time.LocalDateTime;

public class JsonUtilsTest {
    @Test
    public void readPositions() {
        LocationPoint read = JsonUtils.read("{\"time\":\"2018-05-02 05:20:31:524\",\"longitude\":1.0,\"latitude\":1.0}", LocationPoint.class);
        System.out.println(read.getTime());
    }

    @Test
    public void writePosition() {
        LocationPoint build = LocationPoint.builder().latitude(1.0).longitude(1.0).time(LocalDateTime.now()).build();
        String write = JsonUtils.write(build);
        System.out.println(write);
    }
}
