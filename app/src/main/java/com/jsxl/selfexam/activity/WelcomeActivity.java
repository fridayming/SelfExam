package com.jsxl.selfexam.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.jsxl.selfexam.R;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("USERINFO",MODE_PRIVATE);
        isLogin = sp.getBoolean("isLogin",false);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogin) {
                    Intent intent = new Intent(WelcomeActivity.this, HomePagerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_show_anim, R.anim.activity_hide_anim);
                    finish();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
                    intent.putExtra("from","welcome");
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_show_anim, R.anim.activity_hide_anim);
                    finish();
                }
            }
        }, 1000);
    }
}
