package com.example.yuze.navigationdrawerdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yuze.navigationdrawerdemo.dto.SignInRequest;
import com.example.yuze.navigationdrawerdemo.dto.SignInResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

public class Login extends AppCompatActivity {
    private EditText usernameEtxt;
    private EditText passwdEtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button loginBtn = findViewById(R.id.login_btn_login);
        Button registerBtn = findViewById(R.id.login_btn_register);
        Button loginOutBtn = findViewById(R.id.login_btn_cancle);
        usernameEtxt = findViewById(R.id.login_edit_account);
        passwdEtxt = findViewById(R.id.login_edit_pwd);
        CheckBox rememberPassword = findViewById(R.id.login_remember);

        loginBtn.setOnClickListener(m_login_listener);
        registerBtn.setOnClickListener(m_login_listener);
        loginOutBtn.setOnClickListener(m_login_listener);
    }

    View.OnClickListener m_login_listener = v -> {
            switch (v.getId()) {
                case R.id.login_btn_login:
                    signIn();
                    break;
                case R.id.login_btn_register:
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn_cancle:
                    new SignOutTask().execute();
                    State.INSTANCE.sessionId = null;
                    break;
            }
    };

    public void signIn() {
        final SignInRequest signInRequest = SignInRequest.builder()
                .userName(usernameEtxt.getText().toString())
                .password(passwdEtxt.getText().toString())
                .build();
        final String signInRequestJson = JsonUtils.write(signInRequest);
        new LoginTask().execute(signInRequestJson);
    }

    private class SignOutTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpUtils.delete(Constants.HOST + Constants.SESSIONS + "/" + State.INSTANCE.sessionId);
            return null;
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            final SignInResponse signInResponse = JsonUtils.read(s, SignInResponse.class);
            if (signInResponse.getSession() == null) {
                Log.i("session",signInResponse.getSession());
                Toast.makeText(Login.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
                State.INSTANCE.sessionId = signInResponse.getSession();
                State.INSTANCE.userId = signInResponse.getUserId();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return HttpUtils.post(
                    Constants.HOST + Constants.SESSIONS,
                    strings[0]);
        }
    }
}
