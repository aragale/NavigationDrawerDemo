package com.example.yuze.navigationdrawerdemo;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

@Layout(R.layout.drawer_item)
public class DrawerMenuItem{

    public static final int DRAWER_MENU_ITEM_LOGIN = 1;
    public static final int DRAWER_MENU_ITEM_FOOTPRINTS = 2;
    public static final int DRAWER_MENU_ITEM_SHARE = 3;
    public static final int DRAWER_MENU_ITEM_CLOUD = 4;
    public static final int DRAWER_MENU_ITEM_SETTINGS = 5;

    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;

    public DrawerMenuItem(Context context, int menuPosition) {
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onCreateMenu() {
        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_LOGIN:
                itemNameTxt.setText("登录");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_account_circle_black_18dp));
                break;
            case DRAWER_MENU_ITEM_FOOTPRINTS:
                itemNameTxt.setText("足迹");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_footprints_black_18dp));
                break;
            case DRAWER_MENU_ITEM_SHARE:
                itemNameTxt.setText("分享");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_share_black_18dp));
                break;
            case DRAWER_MENU_ITEM_CLOUD:
                itemNameTxt.setText("云同步");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_cloud_upload_black_18dp));
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                itemNameTxt.setText("设置");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_settings_black_24dp));
                break;
        }
    }

    @Click(R.id.mainView)
    public void onMenuItemClick(){
        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_LOGIN:
                Intent intent = new Intent(mContext,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "Profile", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onLoginMenuSelected();
                break;
            case DRAWER_MENU_ITEM_FOOTPRINTS:
                Toast.makeText(mContext, "Requests", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onFootprintsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_SHARE:
                Toast.makeText(mContext, "Messages", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onShareMenuSelected();
                break;
            case DRAWER_MENU_ITEM_CLOUD:
                Toast.makeText(mContext, "Notifications", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onCloudMenuSelected();
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                Toast.makeText(mContext, "Settings", Toast.LENGTH_SHORT).show();
                if(mCallBack != null)mCallBack.onSettingsMenuSelected();
                break;
        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack{
        void onLoginMenuSelected();
        void onFootprintsMenuSelected();
        void onShareMenuSelected();
        void onCloudMenuSelected();
        void onSettingsMenuSelected();
    }

}
