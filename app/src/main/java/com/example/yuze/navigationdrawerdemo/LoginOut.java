package com.example.yuze.navigationdrawerdemo;

import android.os.AsyncTask;

import com.example.yuze.navigationdrawerdemo.dto.SignInResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

public class LoginOut {

    public void loginOut() {
        if (State.INSTANCE.sessionId == null) {
//         Toast.makeText(getClass(),"您未登陆",Toast.LENGTH_SHORT).show();
        } else {
            new SignOutTask().execute(State.INSTANCE.sessionId);
        }
    }

    private class SignOutTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final SignInResponse signInResponse = JsonUtils.read(s, SignInResponse.class);
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.delete(Constants.HOST + Constants.SESSIONS + "/" + State.INSTANCE.userId, strings[0]);
        }
    }
}
