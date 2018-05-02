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

public class FootPrint extends AppCompatActivity {

    private Button startbtn;
    private Button pausebtn;
    private Button stopbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foot_print);

        startbtn = findViewById(R.id.start_trip);
        pausebtn = findViewById(R.id.pause_trip);
        stopbtn = findViewById(R.id.end_trip);

        stopbtn.setOnClickListener(m_PositionPoints_listener);
        pausebtn.setOnClickListener(m_PositionPoints_listener);
        stopbtn.setOnClickListener(m_PositionPoints_listener);
    }

    View.OnClickListener m_PositionPoints_listener = v -> {
        switch (v.getId()){
            case R.id.start_trip:
                //((MyApplication)getApplication()).initLocation();
                Toast.makeText(this,"开始记录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.pause_trip:
                break;
            case R.id.end_trip:
                locationPoints();
                Toast.makeText(this,"记录结束",Toast.LENGTH_SHORT).show();
                break;
        }
    };

    public void locationPoints() {
        //获取Application里的位置列表
        List<LocationPoint> list = ((MyApplication) getApplication()).locationPoints;
        final String locationPointsRequestJson = JsonUtils.write(list);
        //清空列表
        list.clear();
        //启动线程
        new LocationPointsTask().execute(locationPointsRequestJson);
    }

    private class LocationPointsTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final LocationPointsResponse locationPointsResponse = JsonUtils.read(s, LocationPointsResponse.class);
            if (locationPointsResponse.getId() == null){
                Log.e("locationPoints","get location points err");
            }else {
                Log.i("locationPoints", s);
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
