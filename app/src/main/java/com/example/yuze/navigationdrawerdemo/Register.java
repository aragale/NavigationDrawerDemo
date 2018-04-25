package com.example.yuze.navigationdrawerdemo;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yuze.navigationdrawerdemo.dto.SignUpRequest;
import com.example.yuze.navigationdrawerdemo.dto.SignUpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Register extends AppCompatActivity {

    private EditText usernameEtxt;   //用户名编辑
    private EditText passwdEtxt;       //密码编辑
    private EditText pwdcheckEtxt;  //密码编辑
    private Button mSureButton;  //确定按钮
    private Button mCancelButton;//取消按钮

    final ObjectMapper objectMapper = new ObjectMapper();

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

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
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
        if(usernameEtxt.getText() == null){
            Log.i("username",usernameEtxt.getText().toString());
            Toast.makeText(this,getString(R.string.account_empty),Toast.LENGTH_SHORT).show();
        }else if(passwdEtxt.getText() == null){
            Log.i("password",passwdEtxt.getText().toString());
            Toast.makeText(this,getString(R.string.pwd_empty),Toast.LENGTH_SHORT).show();
        }else if (!pwdcheckEtxt.getText().toString().equals(passwdEtxt.getText().toString())){
            Log.i("passwordcheck",pwdcheckEtxt.getText().toString());
            Toast.makeText(this,getString(R.string.pwd_not_same),Toast.LENGTH_SHORT).show();
        }
        else {
            signUp();
        }
    }

    public void signUp() {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
                .userName(usernameEtxt.getText().toString())
                .password(passwdEtxt.getText().toString())
                .build();
        try {
            final String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);
            final String signUpResponseJson = HttpUtils.post(
                    Constants.HOST + Constants.USERS,
                    signUpRequestJson);
            final SignUpResponse signUpResponse = objectMapper.readValue(signUpResponseJson, SignUpResponse.class);
            if (signUpResponse.getId() == null){
                Toast.makeText(this,R.string.register_fail,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,R.string.register_success,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
