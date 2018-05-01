package com.example.yuze.navigationdrawerdemo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yuze.navigationdrawerdemo.dto.LocationPointsRequest;
import com.example.yuze.navigationdrawerdemo.dto.LocationPointsResponse;
import com.example.yuze.navigationdrawerdemo.dto.SignInRequest;
import com.example.yuze.navigationdrawerdemo.dto.SignInResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import java.time.Year;

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
                break;
            case R.id.pause_trip:
                break;
            case R.id.end_trip:
                break;
        }
    };

//    public void LocationPoints() {
//        final LocationPointsRequest locationPointsRequest = LocationPointsRequest.builder()
//                .time()
//                .latitude()
//                .longitude()
//                .build();
//        final String signInRequestJson = JsonUtils.write(LocationPointsRequest);
//        new LocationPointsTask().execute(signInRequestJson);
//    }
//
//    private class LocationPointsTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPostExecute(String s) {
//            final LocationPointsResponse locationPointsResponse = JsonUtils.read(s, LocationPointsResponse.class);
//            if (locationPointsResponse.getPositionId() == null){
//                Log.e("LocationPoints","get location points err");
//            }else {
//
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return HttpUtils.post(
//                    Constants.HOST + Constants.TRACES,
//                    strings[0]);
//        }
//    }

}
