package com.example.yuze.navigationdrawerdemo.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.State;
import com.example.yuze.navigationdrawerdemo.dto.LocationPointsResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

public final class GetTraceTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... strings) {
        String responseJson = HttpUtils.get_with_session(Constants.HOST + Constants.TRACES + "/" + strings[0],
                State.INSTANCE.sessionId);
        final LocationPointsResponse locationPointsResponse = JsonUtils.read(responseJson, LocationPointsResponse.class);
        if (locationPointsResponse.getId() == null) {
            Log.e("GetTraceTask", "获取足迹活动");
        } else {
            State.INSTANCE.positions = locationPointsResponse.getPositions();
        }
        return null;
    }
}
