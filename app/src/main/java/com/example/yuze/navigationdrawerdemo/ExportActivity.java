package com.example.yuze.navigationdrawerdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yuze.navigationdrawerdemo.utils.ShareUtils;

public class ExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareFootPrint(this, null);
        String text = ShareUtils.usernameAndFootIdToMessage(State.INSTANCE.userName, State.INSTANCE.footPrintId);
        ((MyApplication) getApplication()).setClipboard(text);
    }

    public static void shareFootPrint(Context context, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享足迹到"));
    }

}
