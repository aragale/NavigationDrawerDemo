package com.example.yuze.navigationdrawerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yuze.navigationdrawerdemo.dto.SignUpRequest;
import com.example.yuze.navigationdrawerdemo.dto.SignUpResponse;
import com.example.yuze.navigationdrawerdemo.utils.HttpUtils;
import com.example.yuze.navigationdrawerdemo.utils.JsonUtils;

import java.util.concurrent.CompletableFuture;

public class Register extends AppCompatActivity {

    private EditText usernameEtxt;   //用户名编辑
    private EditText passwdEtxt;       //密码编辑
    private EditText pwdcheckEtxt;  //密码编辑
    private Button mSureButton;  //确定按钮
    private Button mCancelButton;//取消按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        usernameEtxt = findViewById(R.id.resetpwd_edit_name);
        passwdEtxt = findViewById(R.id.resetpwd_edit_pwd_old);
        pwdcheckEtxt = findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = findViewById(R.id.register_btn_sure);
        mCancelButton = findViewById(R.id.register_btn_cancel);

        //注册界面两个按钮的监听事件
        mSureButton.setOnClickListener(m_register_Listener);
        mCancelButton.setOnClickListener(m_register_Listener);
//
//        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder()
//                .permitAll()
//                .build();
//        StrictMode.setThreadPolicy(policy);
    }

    View.OnClickListener m_register_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:
                    register_check();
                    break;
                case R.id.register_btn_cancel:
                    Intent intent_Register_to_Login = new Intent(Register.this, Login.class);
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };

    public void register_check() {
        if (usernameEtxt.getText() == null) {
            Log.i("username", usernameEtxt.getText().toString());
            Toast.makeText(this, getString(R.string.account_empty), Toast.LENGTH_SHORT).show();
        } else if (passwdEtxt.getText() == null) {
            Log.i("password", passwdEtxt.getText().toString());
            Toast.makeText(this, getString(R.string.pwd_empty), Toast.LENGTH_SHORT).show();
        } else if (!pwdcheckEtxt.getText().toString().equals(passwdEtxt.getText().toString())) {
            Log.i("passwordcheck", pwdcheckEtxt.getText().toString());
            Toast.makeText(this, getString(R.string.pwd_not_same), Toast.LENGTH_SHORT).show();
        } else {
            signUp();
        }
    }

    //private volatile String signUpResponseJson = null;
    //private volatile int signUpResponseSuccess = 0;

    public void signUp() {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
                .userName(usernameEtxt.getText().toString())
                .password(passwdEtxt.getText().toString())
                .build();
        try {
            final String signUpRequestJson = JsonUtils.write(signUpRequest);
            //String signUpResponseJson = null;
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        signUpResponseSuccess = 2;
                        signUpResponseJson = HttpUtils.post(
                                Constants.HOST + Constants.USERS,
                                signUpRequestJson);
                        signUpResponseSuccess = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                        signUpResponseSuccess = -1;
                    }
                }
            }).start();
            // poll, lun xun
            while (signUpResponseSuccess != 2 && signUpResponseSuccess == 0) {
                TimeUnit.MILLISECONDS.sleep(500L);
            }
            if (signUpResponseSuccess == -1) {
                Toast.makeText(this, R.string.register_fail, Toast.LENGTH_SHORT).show();
            }*/
            //for api capability, change min sdk version to 27, then this warning gone
            final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> HttpUtils.post(
                    Constants.HOST + Constants.USERS,
                    signUpRequestJson));
            future.thenApply(signUpResponseJson -> {
                final SignUpResponse signUpResponse = JsonUtils.read(signUpResponseJson, SignUpResponse.class);
                if (signUpResponse == null || signUpResponse.getId() == null) {
                    Toast.makeText(this, R.string.register_fail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                    finish();
                }
                return null;
            }).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
