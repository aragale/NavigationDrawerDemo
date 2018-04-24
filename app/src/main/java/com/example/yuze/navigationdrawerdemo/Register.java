package com.example.yuze.navigationdrawerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private EditText mPwdCheck;  //密码编辑
    private Button mSureButton;  //确定按钮
    private Button mCancelButton;//取消按钮

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        usernameEtxt = findViewById(R.id.resetpwd_edit_name);
        passwdEtxt = findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = findViewById(R.id.register_btn_sure);
        mCancelButton = findViewById(R.id.register_btn_cancel);

        //注册界面两个按钮的监听事件
        mSureButton.setOnClickListener(m_register_Listener);
        mCancelButton.setOnClickListener(m_register_Listener);
    }

    View.OnClickListener m_register_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:
                    register_check();
                    signUp();
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
            Toast.makeText(this,getString(R.string.account_empty),Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwdEtxt.getText() == null){
            Toast.makeText(this,getString(R.string.pwd_empty),Toast.LENGTH_SHORT).show();
            return;
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
//
            if (signUpResponse.getId() == null){
                Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();
                return;
            }else{
                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
