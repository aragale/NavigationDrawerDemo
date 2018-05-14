package com.example.yuze.navigationdrawerdemo.layout;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuze.navigationdrawerdemo.Constants;
import com.example.yuze.navigationdrawerdemo.R;
import com.example.yuze.navigationdrawerdemo.State;
import com.example.yuze.navigationdrawerdemo.dto.UserNameResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;


@NonReusable
@Layout(R.layout.drawer_header)

public class DrawerHeader {

    @View(R.id.profileImageView)
    ImageView profileImage;

    @View(R.id.nameTxt)
    TextView nameTxt;

    @Resolve
    public void onResolved() {
        if (State.INSTANCE.sessionId == null) {
            nameTxt.setText("未登录");
        } else {
            new DrawerHeaderTask().execute(State.INSTANCE.sessionId);
        }
    }

    private class DrawerHeaderTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final UserNameResponse userNameResponse = JsonUtils.read(s, UserNameResponse.class);
            if (userNameResponse.getUserName() == null) {
                Log.e("username", "get userName err");
            } else {
                nameTxt.setText(userNameResponse.getUserName());
                State.INSTANCE.userName = userNameResponse.getUserName();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.get(Constants.HOST + Constants.USERS + "/" + State.INSTANCE.userId, strings[0]);
        }
    }
}
