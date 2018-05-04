package com.example.yuze.navigationdrawerdemo.layout;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.MyApplication;
import com.example.yuze.navigationdrawerdemo.R;
import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;
import com.example.yuze.navigationdrawerdemo.dto.LocationPointsResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import java.util.concurrent.ConcurrentLinkedDeque;

public class NewFootPrintFragment extends Fragment implements View.OnClickListener {

//    private boolean running;//是否运行
//    private boolean wasRunning;//暂停前是否运行

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            //从 savedInstanceState Bundle 中恢复变量状态
//            running = savedInstanceState.getBoolean("running");
//            wasRunning = savedInstanceState.getBoolean("wasRunning");
//            if (wasRunning) {
//                running = true;
//            }
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置片段布局
        View layout = inflater.inflate(R.layout.new_foot_print, container, false);
        //启动 Run() 方法并传入布局
//        Run(layout);

        Button startBtn = layout.findViewById(R.id.start_trace);
        Button stopBtn = layout.findViewById(R.id.end_trace);
        Button editBtn = layout.findViewById(R.id.trace_edit);

        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        return layout;
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        wasRunning = running;//若片段暂停，记录原来是否在运行
//        running = false;//将记录停止
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //若暂停前记录在运行，再设置为运行
//        if (wasRunning) {
//            running = true;
//        }
//    }

    //    public void onSavedInstanceState(Bundle savedInstanceState){
//        //活动撤销前将变量放入 Bundle 中
//        savedInstanceState.putBoolean("running",running);
//        savedInstanceState.putBoolean("wasRunning",wasRunning);
//    }

//    public void onClickStart(View view) {
//        running = true;
//    }
//
//    public void onClickStop(View view) {
//        running = false;
//    }
//
//    private void Run(View view) {
//        final Handler handler = new Handler();
//        handler.post(() -> {
//            if (running) {
//                startTrace();
//            } else {
//                endTrace();
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_trace:
                startTrace();
                Toast.makeText(getContext(), "开始记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.end_trace:
                endTrace();
                Toast.makeText(getContext(), "记录结束", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void startTrace() {
        ((MyApplication) getActivity().getApplication()).locationPoints.clear();
        ((MyApplication) getActivity().getApplication()).isRequestLocation = true;
    }

    private void endTrace() {
        ((MyApplication) getActivity().getApplication()).isRequestLocation = false;
        uploadTrace();
        ((MyApplication) getActivity().getApplication()).locationPoints.clear();
    }

    public void uploadTrace() {
        //获取Application里的位置列表
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
