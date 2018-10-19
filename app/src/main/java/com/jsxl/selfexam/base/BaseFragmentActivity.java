package com.jsxl.selfexam.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jsxl.selfexam.R;

/**
 * Created by Charles on 2018/6/12.
 */

public abstract class BaseFragmentActivity extends FragmentActivity implements View.OnClickListener{
    public abstract void initView();
    public abstract void initData();

    @Override
    public void onClick(View v) {

    }
    public void setStatusColorForBanner(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(false);//false则状态栏会被顶上去
            tintManager.setStatusBarTintResource(R.color.colorStatus);// 通知栏所需颜色
        }
    }
    public void setStatusColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
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