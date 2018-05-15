package com.example.yuze.navigationdrawerdemo.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.yuze.navigationdrawerdemo.State;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;

public final class GetFootPrintImagesTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        State.INSTANCE.footPrintImages.clear();
        for (String url : State.INSTANCE.fpResponse.getImages()) {
            Bitmap bitmap = HttpUtils.getBitmap(url);
            if (bitmap != null) {
                State.INSTANCE.footPrintImages.add(bitmap);
            }
        }
        return null;
    }
}
