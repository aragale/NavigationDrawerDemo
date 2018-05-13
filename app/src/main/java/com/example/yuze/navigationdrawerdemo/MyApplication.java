package com.example.yuze.navigationdrawerdemo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
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
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    public BaiduMap mBaiDuMap = null;

    /**
     * 请求定位线程
     */
    public Thread requestLocationThread = null;

    /**
     * 是否需要请求定位
     */
    public volatile boolean isRequestLocation = false;
    private LocationClient mLocationClient = null;
    private volatile boolean isFirstLocation = true;//是否首次定位 防止每次定位都重新设置中心点和marker
    private MyLocationListener mListener = new MyLocationListener();

    /**
     * 最近一次的纬度
     */
    private volatile double latitude = 0.0;

    /**
     * 最近一次的经度
     */
    private volatile double longitude = 0.0;

    /**
     * 定位点队列
     */
    public final ConcurrentLinkedDeque<LocationPoint> locationPoints = new ConcurrentLinkedDeque<>();

    /**
     * OSS 客户端实例
     */
    public OSSClient ossClient = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

        //请求定位线程
        requestLocationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
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

        //初始化OSS客户端实例
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        //OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
        //insecure
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                Constants.OSS_ACCESS_ID,
                Constants.OSS_ACCESS_SECRET_KEY);

        this.ossClient = new OSSClient(getApplicationContext(), Constants.OSS_END_POINT, credentialProvider, conf);
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

        // 设置定位方式的优先级。
        // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        option.setPriority(LocationClientOption.GpsFirst);

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

            //设置marker半径
            location.setRadius(100);

            Log.i("onReceiveLocation", String.format("latitude:%f, longitude:%f", latitude, longitude));

            if (isRequestLocation && (locationPoints.isEmpty() || checkLocationPoint(location))) {
                Toast.makeText(getApplicationContext(), String.format("lat:%f, lon:%f, t:%s", latitude, longitude, time), Toast.LENGTH_SHORT).show();
                //将经纬度添加到列表
                locationPoints.add(LocationPoint.builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .time(location.getTime())
                        .build());
            }

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                isFirstLocation = false;
            }
            //设置并显示中心点
            setPosition2Center(mBaiDuMap, location, true);
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

    /**
     * 检查制定的定位是否符合要求
     *
     * @param location 指定的com.baidu.location.BDLocation实例
     * @return 若符合要求，返回true，否则返回false
     */
    private boolean checkLocationPoint(final BDLocation location) {
        //队尾
        final LocationPoint last = locationPoints.peekLast();
        //定位实例的时间转换为LocalDateTime
        final LocalDateTime locationTime =
                LocalDateTime.parse(location.getTime(), Constants.BAIDU_LOCATION_TIME_FORMATTER);
        final LocalDateTime lastTime =
                LocalDateTime.parse(last.getTime(), Constants.BAIDU_LOCATION_TIME_FORMATTER);
        //位置是否相同
        boolean sameLocation = location.getLongitude() == last.getLongitude() && location.getLatitude() == last.getLatitude();
        //时间大于30秒
        boolean elapsedGET30Seconds = lastTime.until(locationTime, ChronoUnit.SECONDS) >= 30L;
        return !sameLocation && elapsedGET30Seconds;
    }
}
