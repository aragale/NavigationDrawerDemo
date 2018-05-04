package com.example.yuze.navigationdrawerdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.yuze.navigationdrawerdemo.layout.MyImageView;
import com.example.yuze.navigationdrawerdemo.layout.NewFootPrintFragment;
import com.example.yuze.navigationdrawerdemo.layout.SelectFootPrintFragment;

public class FootPrint extends AppCompatActivity implements View.OnClickListener {

    private int mTextNormalColor;// 未选中的字体颜色
    private int mTextSelectedColor;// 选中的字体颜色

    private LinearLayout mNewFootPrint;
    private LinearLayout mSelectFootPrint;

    private MyImageView mNewImage;
    private MyImageView mSelectImage;

    private NewFootPrintFragment newFootPrintFragment;
    private SelectFootPrintFragment selectFootPrintFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foot_print);

        mNewFootPrint = findViewById(R.id.ll_new);
        mSelectFootPrint = findViewById(R.id.ll_select);

        mNewImage = findViewById(R.id.iv1);
        mSelectImage = findViewById(R.id.iv2);

        mNewFootPrint.setOnClickListener(this);
        mSelectFootPrint.setOnClickListener(this);

        setDefaultFragment();
    }

    private void setDefaultFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        newFootPrintFragment = new NewFootPrintFragment();
        transaction.replace(R.id.content,newFootPrintFragment);
        transaction.commit();
    }

    private void initSelectImage() {
        mNewImage.setImages(R.drawable.ic_add_black_18dp, R.drawable.ic_add_white_18dp);
        mSelectImage.setImages(R.drawable.ic_storage_black_18dp, R.drawable.ic_storage_white_18dp);
    }

    private void initColor() {
        mTextNormalColor = getResources().getColor(R.color.main_bottom_tab_textcolor_normal);
        mTextSelectedColor = getResources().getColor(R.color.main_bottom_tab_textcolor_selected);
    }

    @Override
    public void onClick(View view){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.iv1:
                if (newFootPrintFragment == null){
                    newFootPrintFragment = new NewFootPrintFragment();
                }
                transaction.replace(R.id.content,newFootPrintFragment);
                break;
            case R.id.iv2:
                if (selectFootPrintFragment == null){
                    selectFootPrintFragment = new SelectFootPrintFragment();
                }
                transaction.replace(R.id.content,selectFootPrintFragment);
                break;
        }
        initColor();
        initSelectImage();
        transaction.commit();
    }

}
