package com.example.yuze.navigationdrawerdemo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    public BaiduMap mBaiduMap = null;

    /**
     * 请求定位线程
     */
    public Thread requestLocationThread = null;

    /**
     * 是否需要请求定位
     */
    public volatile boolean isRequestLocation = false;

    private LocationClient mLocationClient = null;
    private volatile boolean isFirstLocation = true;//防止每次定位都重新设置中心点和marker
    private BDAbstractLocationListener mListener = new MyLocationListener();

    /**
     * 最近一次的纬度
     */
    private volatile double latitude = 0.0;

    /**
     * 最近一次的经度
     */
    private volatile double longitude = 0.0;

    public final List<LocationPoint> locationPoints = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //请求定位线程
        requestLocationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()){
                if (isRequestLocation) {
                    //开始定位
                    int requestLocation = mLocationClient.requestLocation();
                    Log.i("requestLocation", String.valueOf(requestLocation));
                }
                try {
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    Log.e("Location request", "InterruptedException", e);
                }
            }
        });
        requestLocationThread.setName("requestLocationThread");
        requestLocationThread.start();
    }

    public void initLocation() {
        LocationClientOption option = new LocationClientOption();

        mLocationClient = new LocationClient(this);//声明LocationClient类
        mLocationClient.registerLocationListener(mListener);//注册监听函数

        //设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(3000);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 实现定位监听 位置一旦有所改变就会调用这个方法
     * 可以在这个方法里面获取到定位之后获取到的一系列数据
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //经纬度
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String time = location.getTime();
            Toast.makeText(getApplicationContext(), String.format("lat:%f, lon:%f, t:%s", latitude, longitude, time), Toast.LENGTH_SHORT).show();

            //设置marker半径
            location.setRadius(100);

            Log.i("onReceiveLocation", String.format("latitude:%f, longitude:%f", latitude, longitude));

            //将经纬度添加到列表
            locationPoints.add(LocationPoint.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .time(location.getTime())
                    .build());

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                isFirstLocation = false;
            }
            //设置并显示中心点
            setPosition2Center(mBaiduMap, location, true);
        }
    }

    /**
     * 设置中心点和添加marker
     */
    public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .build();
        map.setMyLocationData(locData);

        if (isShowLoc) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }
}
