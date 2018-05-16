package com.example.yuze.navigationdrawerdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;

import java.util.ArrayList;

public class GetFPActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        ViewSwitcher.ViewFactory {

    private TextureMapView mMapView;
    private BaiduMap mBaiDuMap;
    private ImageSwitcher imageSwitcher;

    private String footPrintID;

    /**
     * 图片列表
     */
    private ArrayList<Drawable> images = new ArrayList<>();

    private TextView title;
    private TextView description;
    private String traceId;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.footPrintID = State.INSTANCE.fpResponse.getId();

        //设置窗口无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_foot_print);

        if (State.INSTANCE.sessionId == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        //初始化图片列表
        for (Bitmap bitmap : State.INSTANCE.footPrintImages) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageBitmap(bitmap);
            images.add(imageView.getDrawable());
        }

        title = findViewById(R.id.foot_print_title);
        description = findViewById(R.id.foot_print_description);

        title.setText(State.INSTANCE.fpResponse.getTitle());
        description.setText(State.INSTANCE.fpResponse.getDescription());

        mMapView = findViewById(R.id.textureMap);
        mBaiDuMap = mMapView.getMap();
        //((MyApplication) getApplication()).mBaiDuMap = mBaiDuMap;
        //设置地图类型为普通图
        mBaiDuMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //显示指南针
        mBaiDuMap.setCompassEnable(true);
        //关闭缩放按钮
        mMapView.showZoomControls(true);
        imageSwitcher = findViewById(R.id.switcher);
        //注意在使用一个ImageSwitcher之前一定要调用setFactory方法，否则setImageResource方法报空指针异常。
        imageSwitcher.setFactory(this);
        //设置动画效果
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        Gallery gallery = findViewById(R.id.gallery);
        //添加OnItemSelectedListener监听器
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemSelectedListener(this);

        drawRoute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        imageSwitcher.setImageDrawable(images.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return imageView;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return State.INSTANCE.footPrintImages.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);
            i.setImageBitmap(State.INSTANCE.footPrintImages.get(position));
            //i.setImageBitmap(BitmapFactory.decodeFile("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1524323161888.jpg"));
            //设置边界对齐
            i.setAdjustViewBounds(true);
            //设置布局参数
            i.setLayoutParams(new Gallery.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return i;
        }
    }

    private void drawRoute() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        if (State.INSTANCE.positions.size() >= 2) {
            //获取最后一个点
            LocationPoint last = State.INSTANCE.positions.get(State.INSTANCE.positions.size() - 1);
            //设置视图到最后一个点
            MapStatus.Builder builder = new MapStatus.Builder();
            LatLng ll = new LatLng(last.getLatitude(), last.getLongitude());
            builder.target(ll).zoom(18.0f);
            mBaiDuMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            for (LocationPoint p : State.INSTANCE.positions) {
                latLngs.add(new LatLng(p.getLatitude(), p.getLongitude()));
            }
            OverlayOptions overlayOptions = new PolylineOptions().width(6)
                    .color(0xaaff0000)
                    .points(latLngs);
            polyline = (Polyline) mBaiDuMap.addOverlay(overlayOptions);
        }
    }
}
