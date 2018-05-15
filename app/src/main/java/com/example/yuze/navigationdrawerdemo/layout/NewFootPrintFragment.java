package com.example.yuze.navigationdrawerdemo.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.MyApplication;
import com.example.yuze.navigationdrawerdemo.R;
import com.example.yuze.navigationdrawerdemo.State;
import com.example.yuze.navigationdrawerdemo.dto.FPRequest;
import com.example.yuze.navigationdrawerdemo.dto.FPResponse;
import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;
import com.example.yuze.navigationdrawerdemo.dto.LocationPointsResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NewFootPrintFragment extends Fragment implements View.OnClickListener {

    private TextureMapView mMapView;
    private BaiduMap mBaiDuMap;
    private Polyline mPolyLine;

    private Button startBtn;
    private Button stopBtn;
    private LocationPoint lastLocationPoint = null;

    private FloatingActionButton mFab;
    private FloatingActionButton photoButton;
    private FloatingActionButton desButton;
    private LinearLayout photoLayout;
    private LinearLayout editLayout;

    private String title = null;
    private String description = null;

    private final List<String> urls = new ArrayList<>();

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

        mFab = layout.findViewById(R.id.editLayout);
        photoButton = layout.findViewById(R.id.photoButton);
        desButton = layout.findViewById(R.id.desButton);
        photoLayout = layout.findViewById(R.id.photo);
        editLayout = layout.findViewById(R.id.edit);
        photoLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.GONE);

        mMapView = layout.findViewById(R.id.mMap);
        mBaiDuMap = mMapView.getMap();
        //k开启定位图层
        mBaiDuMap.setMyLocationEnabled(true);
        //关闭缩放按钮
        mMapView.showZoomControls(false);
        ((MyApplication) getActivity().getApplication()).mBaiDuMap = mBaiDuMap;

        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        mFab.setOnClickListener(v -> {
            if (photoLayout.getVisibility() == View.VISIBLE && editLayout.getVisibility() == View.VISIBLE) {
                photoLayout.setVisibility(View.GONE);
                editLayout.setVisibility(View.GONE);
            } else {
                photoLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.VISIBLE);
            }
        });

        photoButton.setOnClickListener(v -> {
            openAlbum();
            onActivityResult(0, 0, getActivity().getIntent());
        });

        desButton.setOnClickListener(v -> {
            alertDialog();
        });

        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_trace:
                //开始记录
                startTrace();
                startBtn.setBackgroundColor(Color.rgb(46, 139, 87));
                Toast.makeText(getActivity().getApplicationContext(), "开始记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.end_trace:
                //记录结束
                endTrace();
                startBtn.setBackgroundColor(Color.rgb(176, 196, 222));
                Toast.makeText(getActivity().getApplicationContext(), "记录结束", Toast.LENGTH_SHORT).show();
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
                    TimeUnit.SECONDS.sleep(Constants.DRAW_TRACE_PERIOD);
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

    /**
     * 结束记录路途
     */
    private void endTrace() {
        //停止定位
        ((MyApplication) getActivity().getApplication()).isRequestLocation = false;
        //上传位置信息
        uploadTrace();
        //清空位置信息队列
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
                State.INSTANCE.traceId = locationPointsResponse.getId();
                Log.w("上传路途", s);
                //上传足迹
                uploadFootPrints();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.post_with_session(
                    Constants.HOST + Constants.TRACES,
                    strings[0]);
        }
    }

    /**
     * 打开系统相册
     */
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 0);
    }

    /**
     * 这个是回调函数
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    //获取长度
                    final int itemCount = data.getClipData().getItemCount();
                    //if 长度不是0
                    if (itemCount != 0) {
                        //创建一个列表，便于后面批量上传到OSS
                        final ArrayList<InputStream> inputStreams = new ArrayList<>(itemCount);
                        //遍历
                        for (int i = 0; i < itemCount; i++) {
                            InputStream inputStream = getActivity()
                                    .getContentResolver()
                                    .openInputStream(data.getClipData().getItemAt(i).getUri());
                            if (inputStream.available() != 0) {
                                inputStreams.add(inputStream);
                            }
                        }
                        //clear
                        this.urls.clear();
                        this.urls.addAll(uploadFilesToOss(inputStreams));
                    }
                } catch (Exception e) {
                    //log error instead of print stack trace....
                    Log.e(NewFootPrintFragment.class.getSimpleName(), "Select photos", e);
                    e.printStackTrace();
                }
            }

        }
    }

    protected void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.description_dialog, null);
        EditText title = v.findViewById(R.id.title_edit);
        EditText description = v.findViewById(R.id.description_edit);
        Button finishBtn = v.findViewById(R.id.finishButton);
        Button cancelBtn = v.findViewById(R.id.cancelButton);
        final Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(v);
        //添加描述「完成」按钮
        finishBtn.setOnClickListener(v1 -> {
            this.title = title.getText().toString();
            this.description = description.getText().toString();
            dialog.dismiss();
            //上传足迹修改内容
            UpdateFootPrints();
        });
        cancelBtn.setOnClickListener(v12 -> {
            dialog.dismiss();
        });
    }

    /**
     * 上传足迹
     */
    public void uploadFootPrints() {
        final FPRequest fpRequest = FPRequest.builder()
                .title(title)
                .description(description)
                .images(this.urls)
                .traceId(State.INSTANCE.traceId)
                .build();
        final String fpRequestJson = JsonUtils.write(fpRequest);
        new UploadFootPrintsTask().execute(fpRequestJson);
    }

    private class UploadFootPrintsTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final FPResponse fpResponse = JsonUtils.read(s, FPResponse.class);
            if (fpResponse.getId() == null) {
                Log.e("上传足迹", "get foot prints err");
            } else {
                State.INSTANCE.footPrintId = fpResponse.getId();
                Log.w("上传足迹", s);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.post_with_session(
                    Constants.HOST + Constants.FootPrints,
                    strings[0]);
        }
    }

    /**
     * 修改足迹task
     */
    public void UpdateFootPrints() {
        final FPRequest fpRequest = FPRequest.builder()
                .title(title)
                .description(description)
                .images(this.urls)
                .traceId(State.INSTANCE.traceId)
                .build();
        final String fpRequestJson = JsonUtils.write(fpRequest);
        new UploadTraceTask().execute(fpRequestJson);
    }

    private class UpdateFootPrintsTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final FPResponse fpResponse = JsonUtils.read(s, FPResponse.class);
            if (fpResponse.getId() == null) {
                Log.e("修改足迹", "get foot prints err");
            } else {
                Log.w("修改足迹", s);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.put_with_session(
                    Constants.HOST + Constants.FootPrints + "/" + State.INSTANCE.footPrintId,
                    strings[0]);
        }
    }

    /**
     * 上传输入流列表，返回对应流的url列表
     *
     * @param inputStreams 输入流列表
     * @return url列表
     */
    private List<String> uploadFilesToOss(final List<InputStream> inputStreams) {
        final OSSClient ossClient = ((MyApplication) getActivity().getApplicationContext()).ossClient;
        final List<String> urls = new ArrayList<>(inputStreams.size());
        //初始计数
        final AtomicInteger doneCount = new AtomicInteger(0);
        //遍历
        for (InputStream is : inputStreams) {
            try {
                final int size = is.available();
                if (size > 0) {
                    final byte[] bytes = new byte[size];
                    final int read = is.read(bytes);
                    is.close();
                    if (read != 0) {
                        final String key = UUID.randomUUID().toString();
                        final String url = Constants.OSS_URL_PREFIX + key + ".jpg";
                        urls.add(url);
                        PutObjectRequest put = new PutObjectRequest(
                                Constants.OSS_BUCKET_NAME,
                                key + ".jpg",
                                bytes);
                        OSSAsyncTask task = ossClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                Log.d("PutObject", url);
                                doneCount.incrementAndGet();
                                if (doneCount.get() == inputStreams.size()) {
                                    Toast.makeText(getActivity(), "图片上传完成", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                // 请求异常
                                Log.d("PutObject", url);
                                if (clientExcepion != null) {
                                    // 本地异常如网络异常等
                                    Log.e("Upload", "本地异常如网络异常等", clientExcepion);
                                }
                                if (serviceException != null) {
                                    // 服务异常
                                    Log.e("ErrorCode", serviceException.getErrorCode());
                                    Log.e("RequestId", serviceException.getRequestId());
                                    Log.e("HostId", serviceException.getHostId());
                                    Log.e("RawMessage", serviceException.getRawMessage());
                                }
                                doneCount.incrementAndGet();
                                if (doneCount.get() == inputStreams.size()) {
                                    Toast.makeText(getActivity(), "图片上传完成", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            } catch (IOException e) {
                Log.e(NewFootPrintFragment.class.getSimpleName(), "上传输入流列表", e);
            }
        }
        return urls;
    }
}