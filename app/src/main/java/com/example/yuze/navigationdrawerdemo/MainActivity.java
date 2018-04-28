package com.example.yuze.navigationdrawerdemo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.mindorks.placeholderview.PlaceHolderView;

public class MainActivity extends AppCompatActivity {

    LocationClient locationClient;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.mTexturemap);//地图初始化
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
        locationClient = new LocationClient(this);

        locationClient.start();

        mDrawer = findViewById(R.id.drawerLayout);
        mDrawerView = findViewById(R.id.drawerView);
        mToolbar = findViewById(R.id.toolbar);
        mGalleryView = findViewById(R.id.galleryView);
        setupDrawer();
    }

    private void setupDrawer() {
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGIN))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FOOTPRINTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SHARE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CLOUD))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();//退出时销毁定位
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);//关闭定位图层
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
//        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
}
