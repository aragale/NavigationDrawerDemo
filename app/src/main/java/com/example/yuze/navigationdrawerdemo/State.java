package com.example.yuze.navigationdrawerdemo;

import android.graphics.Bitmap;

import com.example.yuze.navigationdrawerdemo.dto.FPResponse;
import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局状态
 */
public enum  State {

    INSTANCE;

    /**
     * 会话ID
     */
    public String sessionId;

    /**
     * 用户名称
     */
    public String userName;

    /**
     * 用户ID
     */
    public String userId;

    /**
     * 路径ID
     */
    public String traceId;

    /**
     * 足迹ID
     */
    public String footPrintId;

    /**
     * 足迹
     */
    public FPResponse fpResponse;

    /**
     * 足迹图片列表
     */
    public List<Bitmap> footPrintImages = new ArrayList<>();

    /**
     * 足迹响应数组
     */
    public FPResponse[] fpResponses;

    public List<LocationPoint> positions = new ArrayList<>();
}
