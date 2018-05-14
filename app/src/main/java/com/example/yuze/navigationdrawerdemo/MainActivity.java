package com.example.yuze.navigationdrawerdemo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.example.yuze.navigationdrawerdemo.layout.DrawerHeader;
import com.example.yuze.navigationdrawerdemo.layout.DrawerMenuItem;
import com.mindorks.placeholderview.PlaceHolderView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    private TextureMapView mMapView;
    private BaiduMap mBaiDuMap;
    LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.mTextureMap);
        initMap();
        ((MyApplication) getApplication()).initLocation();
        mDrawer = findViewById(R.id.drawerLayout);
        mDrawerView = findViewById(R.id.drawerView);
        mToolbar = findViewById(R.id.toolbar);
        setupDrawer();

        MainActivityPermissionsDispatcher.ApplySuccessWithPermissionCheck(this);
    }

    /**
     * 添加抽屉式导航栏
     */
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

    /**
     * 地图初始化
     */
    public void initMap() {
        mBaiDuMap = mMapView.getMap();
        ((MyApplication) getApplication()).mBaiDuMap = mBaiDuMap;
        //设置地图类型为普通图
        mBaiDuMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //关闭缩放按钮
        mMapView.showZoomControls(false);
        // 开启定位图层
        mBaiDuMap.setMyLocationEnabled(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 申请权限成功时
     */
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void ApplySuccess() {
    }

    /**
     * 申请权限告诉用户原因时
     *
     * @param request
     */
    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showRationaleForMap(PermissionRequest request) {
        showRationaleDialog("使用此功能需要打开定位的权限", request);
    }

    /**
     * 申请权限被拒绝时
     */
    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onMapDenied() {
        Toast.makeText(this, "你拒绝了权限，该功能不可用", Toast.LENGTH_LONG).show();
    }

    /**
     * 申请权限被拒绝并勾选不再提醒时
     */
    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onMapNeverAskAgain() {
        AskForPermission();
    }

    /**
     * 告知用户具体需要权限的原因
     *
     * @param messageResId
     * @param request
     */
    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", (dialog, which) -> {
                    request.proceed();//请求权限
                })
                .setNegativeButton("取消", (dialog, which) -> request.cancel())
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前应用缺少定位权限,请去设置界面打开\n打开之后按两次返回键可回到该应用哦");
        builder.setNegativeButton("取消", (dialog, which) -> {
            return;
        });
        builder.setPositiveButton("设置", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName())); // 根据包名打开对应的设置界面
            startActivity(intent);
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiDuMap.setMyLocationEnabled(false);//关闭定位图层
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        //initMap();
        //((MyApplication) getApplication()).initLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
}
