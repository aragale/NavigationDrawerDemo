package com.example.yuze.navigationdrawerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;

public class GetFPActivity extends AppCompatActivity {

    private TextureMapView mMapView;
    private BaiduMap mBaiDuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_foot_print);
        mMapView = findViewById(R.id.textureMap);
        mBaiDuMap = mMapView.getMap();
        ((MyApplication) getApplication()).mBaiDuMap = mBaiDuMap;
        //设置地图类型为普通图
        mBaiDuMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //关闭缩放按钮
        mMapView.showZoomControls(false);
        // 开启定位图层
//        mBaiDuMap.setMyLocationEnabled(true);
    }
}
