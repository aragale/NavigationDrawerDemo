package com.example.yuze.navigationdrawerdemo.layout;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.MyApplication;
import com.example.yuze.navigationdrawerdemo.R;
import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;
import com.example.yuze.navigationdrawerdemo.dto.LocationPointsResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class NewFootPrintFragment extends Fragment implements View.OnClickListener {

    private TextureMapView mMapView;
    private BaiduMap mBaiDuMap;
    private Polyline mPolyLine;

    private Button startBtn;
    private Button stopBtn;
    private Button editBtn;
    private LocationPoint lastLocationPoint = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //设置片段布局
        View layout = inflater.inflate(R.layout.new_foot_print, container, false);

        startBtn = layout.findViewById(R.id.start_trace);
        stopBtn = layout.findViewById(R.id.end_trace);
        editBtn = layout.findViewById(R.id.trace_edit);

        mMapView = layout.findViewById(R.id.mMap);
        mBaiDuMap = mMapView.getMap();
        mBaiDuMap.setMyLocationEnabled(true);
        ((MyApplication) getActivity().getApplication()).mBaiDuMap = mBaiDuMap;

        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_trace:
                startTrace();
                startBtn.setBackgroundColor(Color.rgb(46, 139, 87));
                Toast.makeText(getContext(), "开始记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.end_trace:
                endTrace();
                startBtn.setBackgroundColor(Color.rgb(176, 196, 222));
                Toast.makeText(getContext(), "记录结束", Toast.LENGTH_SHORT).show();
                break;
            case R.id.trace_edit:
                break;
        }
    }

    private void drawRoute() {
        //获取Application里的位置队列队尾成员
        LocationPoint queueTail = ((MyApplication) getActivity().getApplication()).locationPoints.peekLast();
        if (queueTail == null) {
            return;
        }
        if (lastLocationPoint == null) {
            //若【最后定位点】为空，则表明当前为初始状态
            lastLocationPoint = queueTail;
        } else if (!Objects.equals(lastLocationPoint.getLatitude(), queueTail.getLatitude()) ||
                !Objects.equals(lastLocationPoint.getLongitude(), queueTail.getLongitude())) {
            //Toast.makeText(getContext(), String.format("绘制，新的定位(%.4f, %.4f)", queueTail.getLatitude(), queueTail.getLongitude()),Toast.LENGTH_SHORT).show();
            //若【队尾位置】与【最后定位点】不同，绘制两点之间的折线
            ArrayList<LatLng> latLngs = new ArrayList<>(2);
            latLngs.add(new LatLng(queueTail.getLatitude(), queueTail.getLongitude()));
            latLngs.add(new LatLng(lastLocationPoint.getLatitude(), lastLocationPoint.getLongitude()));
            OverlayOptions ooPolyLine = new PolylineOptions().width(6).color(0xAAFF0000).points(latLngs);
            //绘制折线
            mPolyLine = (Polyline) mBaiDuMap.addOverlay(ooPolyLine);
            //覆盖【最后定位点】
            lastLocationPoint = queueTail;
        }
    }

    private void startTrace() {
        ((MyApplication) getActivity().getApplication()).locationPoints.clear();
        ((MyApplication) getActivity().getApplication()).isRequestLocation = true;
        //绘制线程
        Thread drawRouteThread = new Thread(() -> {
            //当停止定位时，循环结束，线程结束
            while (((MyApplication) getActivity().getApplication()).isRequestLocation) {
                try {
                    TimeUnit.SECONDS.sleep(30L);
                } catch (InterruptedException e) {
                    Log.e("Route Paint", "InterruptedException", e);
                }
                drawRoute();
            }
        });
        drawRouteThread.setName("drawRouteThread");
        //运行线程
        drawRouteThread.start();
    }

    private void endTrace() {
        //停止定位
        ((MyApplication) getActivity().getApplication()).isRequestLocation = false;
        uploadTrace();
        ((MyApplication) getActivity().getApplication()).locationPoints.clear();
    }

    public void uploadTrace() {
        //获取Application里的位置队列
        ConcurrentLinkedDeque<LocationPoint> list = ((MyApplication) getActivity().getApplication()).locationPoints;
        final String locationPointsRequestJson = JsonUtils.write(list);
        //启动线程
        new UploadTraceTask().execute(locationPointsRequestJson);
    }

    private class UploadTraceTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final LocationPointsResponse locationPointsResponse = JsonUtils.read(s, LocationPointsResponse.class);
            if (locationPointsResponse.getId() == null) {
                Log.e("上传路途", "get location points err");
            } else {
                Log.w("上传路途", s);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.post_with_session(
                    Constants.HOST + Constants.TRACES,
                    strings[0]);
        }
    }
}
