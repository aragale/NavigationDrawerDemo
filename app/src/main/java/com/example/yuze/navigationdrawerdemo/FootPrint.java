package com.example.yuze.navigationdrawerdemo;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuze.navigationdrawerdemo.layout.SelectFootPrintFragment;
import com.example.yuze.navigationdrawerdemo.layout.NewFootPrintFragment;
import com.example.yuze.navigationdrawerdemo.layout.MyAdapter;
import com.example.yuze.navigationdrawerdemo.layout.MyImageView;

import java.util.ArrayList;

public class FootPrint extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private MyImageView mIvHome; // tab 消息的imageview
    private TextView mTvHome;   // tab 消息的imageview

    private MyImageView mIvCategory; // tab 通讯录的imageview
    private TextView mTvCategory;

    private ArrayList<Fragment> mFragments;
    private ArgbEvaluator mColorEvaluator;

    private int mTextNormalColor;// 未选中的字体颜色
    private int mTextSelectedColor;// 选中的字体颜色
    private LinearLayout mLinearLayoutHome;
    private LinearLayout mLinearLayoutCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foot_print);
        initColor();//也就是选中未选中的textview的color
        initView();// 初始化控件
        initData(); // 初始化数据(也就是fragments)
        initSelectImage();// 初始化渐变的图片
        aboutViewpager(); // 关于viewpager
        setListener(); // viewpager设置滑动监听
    }

    private void initSelectImage() {
        mIvHome.setImages(R.drawable.ic_add_black_18dp, R.drawable.ic_add_white_18dp);
        mIvCategory.setImages(R.drawable.ic_storage_black_18dp, R.drawable.ic_storage_white_18dp);
    }

    private void initColor() {
        mTextNormalColor = getResources().getColor(R.color.main_bottom_tab_textcolor_normal);
        mTextSelectedColor = getResources().getColor(R.color.main_bottom_tab_textcolor_selected);
    }


    private void setListener() {
        //下面的tab设置点击监听
        mLinearLayoutHome.setOnClickListener(this);
        mLinearLayoutCategory.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPs) {
                setTabTextColorAndImageView(position, positionOffset);// 更改text的颜色还有图片
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setTabTextColorAndImageView(int position, float positionOffset) {
        mColorEvaluator = new ArgbEvaluator();  // 根据偏移量 来得到
        int evaluateCurrent = (int) mColorEvaluator.evaluate(positionOffset, mTextSelectedColor, mTextNormalColor);//当前tab的颜色值
        int evaluateThe = (int) mColorEvaluator.evaluate(positionOffset, mTextNormalColor, mTextSelectedColor);// 将要到tab的颜色值
        switch (position) {
            case 0:
                mTvHome.setTextColor(evaluateCurrent);  //设置消息的字体颜色
                mTvCategory.setTextColor(evaluateThe);  //设置通讯录的字体颜色

                mIvHome.transformPage(positionOffset);  //设置消息的图片
                mIvCategory.transformPage(1 - positionOffset); //设置通讯录的图片
                break;
            case 1:
                mTvCategory.setTextColor(evaluateCurrent);

                mIvCategory.transformPage(positionOffset);
                break;


        }
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new NewFootPrintFragment());
        mFragments.add(new SelectFootPrintFragment());

    }

    private void aboutViewpager() {
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), mFragments);// 初始化adapter
        mViewPager.setAdapter(myAdapter); // 设置adapter
    }

    private void initView() {
        mLinearLayoutHome = findViewById(R.id.ll_new);
        mLinearLayoutCategory = findViewById(R.id.ll_select);

        mViewPager =  findViewById(R.id.vp);
        mIvHome =  findViewById(R.id.iv1);  // tab 微信 imageview
        mTvHome =  findViewById(R.id.rb1);  //  tab  微信 字

        mIvCategory =  findViewById(R.id.iv2); // tab 通信录 imageview
        mTvCategory =  findViewById(R.id.rb2);  // tab   通信录 字


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.ll_select:
                mViewPager.setCurrentItem(1);
                break;
        }
    }
}
