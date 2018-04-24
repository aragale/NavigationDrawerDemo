package com.example.yuze.navigationdrawerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private Button loginbtn;
    private Button registebtn;
    private Button login_cancle_btn;
    private EditText usernameEtxt;
    private EditText passwdEtxt;
    private CheckBox rememberpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginbtn =  findViewById(R.id.login_btn_login);
        registebtn = findViewById(R.id.login_btn_register);
        login_cancle_btn = findViewById(R.id.login_btn_cancle);
        usernameEtxt = findViewById(R.id.login_edit_account);
        passwdEtxt = findViewById(R.id.login_edit_pwd);
        rememberpw = findViewById(R.id.login_remember);

        loginbtn.setOnClickListener(m_login_listener);
        registebtn.setOnClickListener(m_login_listener);
        login_cancle_btn.setOnClickListener(m_login_listener);
    }

    View.OnClickListener m_login_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_btn_login:
                    break;
                case R.id.login_btn_register:
                    Intent intent = new Intent(Login.this,Register.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn_cancle:
                    break;
            }
        }
    };
}
