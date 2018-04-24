package com.example.yuze.navigationdrawerdemo;

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
        registebtn = findViewById(R.id.login_btn_login);
        login_cancle_btn = findViewById(R.id.login_btn_cancle);
        usernameEtxt = findViewById(R.id.login_edit_account);
        passwdEtxt = findViewById(R.id.login_edit_pwd);
        rememberpw = findViewById(R.id.login_remember);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
