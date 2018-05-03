package com.example.yuze.navigationdrawerdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yuze.navigationdrawerdemo.dto.LocationPoint;
import com.example.yuze.navigationdrawerdemo.dto.LocationPointsResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import java.util.List;

public class NewFootPrint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_foot_print);

        Button startBtn = findViewById(R.id.start_trace);
        Button stopBtn = findViewById(R.id.end_trace);

        startBtn.setOnClickListener(m_PositionPoints_listener);
        stopBtn.setOnClickListener(m_PositionPoints_listener);
    }

    View.OnClickListener m_PositionPoints_listener = v -> {
        switch (v.getId()){
            case R.id.start_trace:
                startTrace();
                Toast.makeText(this,"开始记录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.end_trace:
                endTrace();
                Toast.makeText(this,"记录结束",Toast.LENGTH_SHORT).show();
                break;
        }
    };

    private void startTrace() {
        ((MyApplication) getApplication()).locationPoints.clear();
        ((MyApplication)getApplication()).isRequestLocation = true;
    }

    private void endTrace() {
        ((MyApplication)getApplication()).isRequestLocation = false;
        uploadTrace();
        ((MyApplication) getApplication()).locationPoints.clear();
    }

    public void uploadTrace() {
        //获取Application里的位置列表
        List<LocationPoint> list = ((MyApplication) getApplication()).locationPoints;
        final String locationPointsRequestJson = JsonUtils.write(list);
        //启动线程
        new UploadTraceTask().execute(locationPointsRequestJson);
    }

    private class UploadTraceTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final LocationPointsResponse locationPointsResponse = JsonUtils.read(s, LocationPointsResponse.class);
            if (locationPointsResponse.getId() == null){
                Log.e("上传路途","get location points err");
            }else {
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
