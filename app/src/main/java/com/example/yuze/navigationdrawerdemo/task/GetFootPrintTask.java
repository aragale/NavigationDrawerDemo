package com.example.yuze.navigationdrawerdemo.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.State;
import com.example.yuze.navigationdrawerdemo.dto.FPResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

public final class GetFootPrintTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        String responseJson = HttpUtils.get_with_session(
                Constants.HOST + Constants.FootPrints + "/" + strings[0],
                State.INSTANCE.sessionId);
        final FPResponse fpResponse = JsonUtils.read(responseJson, FPResponse.class);
        if (fpResponse == null || fpResponse.getId() == null) {
            Log.e("GetFPActivity", "获取足迹活动");
        } else {
            State.INSTANCE.fpResponse = fpResponse;
            new GetFootPrintImagesTask().execute();
        }
        return null;
    }
}
