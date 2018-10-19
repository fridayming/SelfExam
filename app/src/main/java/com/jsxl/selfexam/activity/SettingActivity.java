package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.utils.APKVersionCodeUtils;
import com.jsxl.selfexam.utils.ToastUtils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.system.Os.remove;
import static com.jsxl.selfexam.R.id.et_retrieve_phone;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private RelativeLayout rl_setting_info, rl_setting_version,rl_setting_modifypsw;
    private ToggleButton tb_setting_news;
    private TextView tv_setting_version,tv_setting_signout;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        initView();
        initData();

    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_setting_info = (RelativeLayout) findViewById(R.id.rl_setting_info);
        rl_setting_info.setOnClickListener(this);
        rl_setting_version = (RelativeLayout) findViewById(R.id.rl_setting_version);
        rl_setting_version.setOnClickListener(this);
        rl_setting_modifypsw = (RelativeLayout) findViewById(R.id.rl_setting_modifypsw);
        rl_setting_modifypsw.setOnClickListener(this);
        tb_setting_news = (ToggleButton) findViewById(R.id.tb_setting_news);
        tv_setting_version = (TextView) findViewById(R.id.tv_setting_version);
        tv_setting_signout = (TextView) findViewById(R.id.tv_setting_signout);
        tv_setting_signout.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        //设置版本号
        tv_title.setText("设置");
        if(sp.getBoolean("isLogin",false)){
            tv_setting_signout.setVisibility(View.VISIBLE);
        }else{
            tv_setting_signout.setVisibility(View.GONE);
        }
        tv_setting_version.setText("V"+APKVersionCodeUtils.getVerName(this));
        tb_setting_news.setChecked(sp.getBoolean("MessageReminding", false));
        tb_setting_news.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sp.edit().putBoolean("MessageReminding", isChecked).apply();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_setting_info:
                if(sp.getBoolean("isLogin",false)) {
                    intent = new Intent(this, PersonInfoActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtils.showToast(this,"请先登录");
                }
                break;

            case R.id.rl_setting_version:

                break;
            case R.id.rl_setting_modifypsw:
                if(sp.getBoolean("isLogin",false)) {
                    intent = new Intent(this, ModifyPasswordActivity.class);
                    intent.putExtra("from", "modifyPassword");
                    startActivity(intent);
                }else{
                    ToastUtils.showToast(this,"请先登录");
                }
                break;
            case R.id.tv_setting_signout:
                SharedPreferences.Editor et = sp.edit();
                et.remove("isLogin").remove("userId").remove("userName").remove("examName").remove("headUrl");
                et.commit();
                finish();
                break;
        }
    }
}
