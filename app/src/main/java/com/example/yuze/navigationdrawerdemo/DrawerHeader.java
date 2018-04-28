package com.example.yuze.navigationdrawerdemo;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuze.navigationdrawerdemo.dto.SignInResponse;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@NonReusable
@Layout(R.layout.drawer_header)

public class DrawerHeader {

    SignInResponse signInResponse = new SignInResponse();

    @View(R.id.profileImageView)
    ImageView profileImage;

    @View(R.id.nameTxt)
    TextView nameTxt;

    @Resolve
    public void onResolved() {
        if (signInResponse.getSession() == null) {
            nameTxt.setText("未登录");
        } else {

        }
    }

}
