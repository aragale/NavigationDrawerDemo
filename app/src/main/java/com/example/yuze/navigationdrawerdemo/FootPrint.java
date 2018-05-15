package com.example.yuze.navigationdrawerdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuze.navigationdrawerdemo.layout.NewFootPrintFragment;
import com.example.yuze.navigationdrawerdemo.layout.SelectFootPrintFragment;

public class FootPrint extends AppCompatActivity implements View.OnClickListener {

    private ImageView newImage;
    private ImageView selectImage;
    private TextView newTxt;
    private TextView selectTxt;

    private LinearLayout newFootPrint;
    private LinearLayout selectFootPrint;

    private NewFootPrintFragment newFootPrintFragment;
    private SelectFootPrintFragment selectFootPrintFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foot_print);


        newImage = findViewById(R.id.iv1);
        newTxt = findViewById(R.id.rb1);

        selectImage = findViewById(R.id.iv2);
        selectTxt = findViewById(R.id.rb2);

        newFootPrint = findViewById(R.id.ll_new);
        selectFootPrint = findViewById(R.id.ll_select);
        newFootPrint.setOnClickListener(this);
        selectFootPrint.setOnClickListener(this);

        setDefaultFragment();
    }

    protected void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        newFootPrintFragment = new NewFootPrintFragment();
        transaction.replace(R.id.content, newFootPrintFragment);
        newImage.setImageDrawable(getDrawable(R.drawable.ic_add_black_18dp));
        newTxt.setTextColor(Color.rgb(75, 0, 130));
        selectImage.setImageResource(R.drawable.ic_storage_white_18dp);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.ll_new:
                if (newFootPrintFragment == null) {
                    newFootPrintFragment = new NewFootPrintFragment();
                }
                transaction.replace(R.id.content, newFootPrintFragment);
                newImage.setImageResource(R.drawable.ic_add_black_18dp);
                newTxt.setTextColor(Color.rgb(75, 0, 130));
                selectImage.setImageResource(R.drawable.ic_storage_white_18dp);
                selectTxt.setTextColor(Color.rgb(0, 0, 0));
                break;
            case R.id.ll_select:
                if (selectFootPrintFragment == null) {
                    selectFootPrintFragment = new SelectFootPrintFragment();
                }
                transaction.replace(R.id.content, selectFootPrintFragment);
                selectImage.setImageResource(R.drawable.ic_storage_black_18dp);
                selectTxt.setTextColor(Color.rgb(75, 0, 130));
                newImage.setImageResource(R.drawable.ic_add_white_18dp);
                newTxt.setTextColor(Color.rgb(0, 0, 0));
//                selectFootPrintFragment = new SelectFootPrintFragment();
//                SelectFootPrintFragment.GetFootPrintListTask getFootPrintListTask = selectFootPrintFragment.new GetFootPrintListTask();
//                getFootPrintListTask.execute(State.INSTANCE.sessionId);
                break;
        }
        transaction.commit();
    }
}
