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
import com.example.yuze.navigationdrawerdemo.dto.SignUpResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Login extends AppCompatActivity {

    final ObjectMapper objectMapper = new ObjectMapper();
    private Button loginbtn;
    private Button registebtn;
    private Button login_cancle_btn;
    private EditText usernameEtxt;
    private EditText passwdEtxt;
    View.OnClickListener m_login_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_login:
                    signIn();
                    break;
                case R.id.login_btn_register:
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn_cancle:
                    break;
            }
        }
    };
    private CheckBox rememberpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginbtn = findViewById(R.id.login_btn_login);
        registebtn = findViewById(R.id.login_btn_register);
        login_cancle_btn = findViewById(R.id.login_btn_cancle);
        usernameEtxt = findViewById(R.id.login_edit_account);
        passwdEtxt = findViewById(R.id.login_edit_pwd);
        rememberpw = findViewById(R.id.login_remember);

        loginbtn.setOnClickListener(m_login_listener);
        registebtn.setOnClickListener(m_login_listener);
        login_cancle_btn.setOnClickListener(m_login_listener);

    }

    public void signIn() {
        final SignInRequest signInRequest = SignInRequest.builder()
                .userName(usernameEtxt.getText().toString())
                .password(passwdEtxt.getText().toString())
                .build();
        final String signInRequestJson = JsonUtils.write(signInRequest);
        new LoginTask().execute(signInRequestJson);
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
