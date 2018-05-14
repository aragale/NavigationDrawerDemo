package com.example.yuze.navigationdrawerdemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.baidu.mapapi.map.TextureMapView;
import com.example.yuze.navigationdrawerdemo.dto.FPResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class GetFPActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        ViewSwitcher.ViewFactory {

    private TextureMapView mMapView;
    private BaiduMap mBaiDuMap;
    private ImageSwitcher imageSwitcher;

    private String footPrintID = ((MyApplication) getApplication()).footId;
    private List<String> images = new ArrayList<>();
    private Object image[];

    private TextView title;
    private TextView decription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗口无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_foot_print);

        title = findViewById(R.id.foot_print_title);
        decription = findViewById(R.id.foot_print_description);

        mMapView = findViewById(R.id.textureMap);
        mBaiDuMap = mMapView.getMap();
        ((MyApplication) getApplication()).mBaiDuMap = mBaiDuMap;
        //设置地图类型为普通图
        mBaiDuMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //关闭缩放按钮
        mMapView.showZoomControls(false);

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

        new getFootPrintTask().execute(State.INSTANCE.sessionId);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFF0000);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                200, 200
        ));
        return imageView;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

            i.setImageBitmap(BitmapFactory.decodeFile((image[position]).toString()));
            //设置边界对齐
            i.setAdjustViewBounds(true);
            //设置布局参数
            i.setLayoutParams(new Gallery.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return i;
        }
    }

    private class getFootPrintTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final FPResponse fpResponse = JsonUtils.read(s, FPResponse.class);
            if (fpResponse.getId() == null) {
                Log.e("username", "get userName err");
            } else {
                title.setText(fpResponse.getTitle());
                decription.setText(fpResponse.getDescription());
                images = fpResponse.getImages();
                image = images.toArray();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.get_with_session(Constants.HOST + Constants.FootPrints + "/" + footPrintID, strings[0]);
        }
    }
}
