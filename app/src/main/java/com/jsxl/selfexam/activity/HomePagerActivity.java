package com.jsxl.selfexam.activity;

import com.jsxl.selfexam.base.BaseFragmentActivity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseFragmentActivity;
import com.jsxl.selfexam.fragment.ConsultationFragment;
import com.jsxl.selfexam.fragment.HomeFragment;
import com.jsxl.selfexam.fragment.MyFragment;
import com.jsxl.selfexam.fragment.RecordFragment;
import com.jsxl.selfexam.update.ApkUpdateManager;

public class HomePagerActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener, MyFragment.SignSuccessPageChangeListener {
    private RadioGroup rg_group;
    private RadioButton rb_home, rb_record, rb_consultation, rb_my;
    private FrameLayout fragment_container;
    private HomeFragment homeFragment;
    private RecordFragment recordFragment;
    private ConsultationFragment consultationFragment;
    private MyFragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusColorForBanner();
        ApkUpdateManager mUpdateManager;
        mUpdateManager = new ApkUpdateManager(this, this);
        mUpdateManager.dsf();
        initView();
    }

    public void initView() {
        rg_group = (RadioGroup) findViewById(R.id.rg_group);
        rg_group.setOnCheckedChangeListener(this);
        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_record = (RadioButton) findViewById(R.id.rb_record);
        rb_consultation = (RadioButton) findViewById(R.id.rb_consultation);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        rb_home.setChecked(true);
    }

    @Override
    public void initData() {

    }

    public void hideAllFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
            rb_home.setTextColor(Color.parseColor("#333333"));
        }
        if (recordFragment != null) {
            transaction.hide(recordFragment);
            rb_record.setTextColor(Color.parseColor("#333333"));
        }
        if (consultationFragment != null) {
            transaction.hide(consultationFragment);
            rb_consultation.setTextColor(Color.parseColor("#333333"));
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
            rb_my.setTextColor(Color.parseColor("#333333"));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (checkedId) {
            case R.id.rb_home:
                rb_home.setTextColor(Color.parseColor("#4397f7"));
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_container, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case R.id.rb_record:
                rb_record.setTextColor(Color.parseColor("#4397f7"));
                if (recordFragment == null) {
                    recordFragment = new RecordFragment();
                    transaction.add(R.id.fragment_container, recordFragment);

                } else {
                    transaction.show(recordFragment);
                }
                break;
            case R.id.rb_consultation:
                rb_consultation.setTextColor(Color.parseColor("#4397f7"));
                if (consultationFragment == null) {
                    consultationFragment = new ConsultationFragment();
                    transaction.add(R.id.fragment_container, consultationFragment);
                } else {
                    transaction.show(consultationFragment);

                }
                break;
            case R.id.rb_my:
                rb_my.setTextColor(Color.parseColor("#4397f7"));
                if (myFragment == null) {
                    myFragment = new MyFragment(this);
                    transaction.add(R.id.fragment_container, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /***
     * 设置按键的处理
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        // 拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void pageChangeListener() {
        rb_home.setChecked(true);
    }
}
