package com.jsxl.selfexam.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.jsxl.selfexam.R;

/**
 * Created by Charles on 2018/6/7.
 */

public abstract  class BaseActivity extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        initView();
        initData();
    }
    public void initView(){};
    public void initData(){};
    public void setStatusColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorStatus);// 通知栏所需颜色
        }
    }
    public void setStatusColor(String color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor(color));
//            tintManager.setStatusBarTintResource(Color.parseColor(color));// 通知栏所需颜色
        }
    }
    public void setStatusColorForBanner(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(false);//false则状态栏会被顶上去
            tintManager.setStatusBarTintResource(R.color.colorStatus);// 通知栏所需颜色
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}