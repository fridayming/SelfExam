package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.NetWorkUtils;
import com.jsxl.selfexam.utils.ToastUtils;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private LinearLayout ll_old_password;
    private EditText et_modify_old_password, et_modify_new_password, et_modify_new_confirmpassword;
    private TextView tv_submit;
    private String from, mobile;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        // retrievePassword  找回密码   modifyPassword 修改密码
        from = getIntent().getStringExtra("from");
        mobile = getIntent().getStringExtra("mobile");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_old_password = (LinearLayout) findViewById(R.id.ll_old_password);
        et_modify_old_password = (EditText) findViewById(R.id.et_modify_old_password);
        et_modify_new_password = (EditText) findViewById(R.id.et_modify_new_password);
        et_modify_new_confirmpassword = (EditText) findViewById(R.id.et_modify_new_confirmpassword);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("修改密码");
        if (from.equals("retrievePassword")) {
            ll_old_password.setVisibility(View.GONE);
        } else if (from.equals("modifyPassword")) {
            ll_old_password.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                judgePassword();
                break;
        }
    }

    private void judgePassword() {
        String oldpassword, password, repassword;
        if (!NetWorkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
            return;
        } else {
            oldpassword = et_modify_old_password.getText().toString().trim();
            if ((oldpassword.equals("") || oldpassword == null) && from.equals("modifyPassword")) {
                Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            password = et_modify_new_password.getText().toString().trim();
            if (password.equals("") || password == null) {
                Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            repassword = et_modify_new_confirmpassword.getText().toString().trim();
            if (repassword.equals("") || repassword == null) {
                Toast.makeText(this, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(repassword)) {
                Toast.makeText(this, "输入密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() > 15 || password.length() < 6) {
                ToastUtils.showToast(this, "密码长度要在6-15位之间");
                return;
            }
            operation();
        }
    }

    private void operation() {
        //修改密码 显示旧密码
        if (from.equals("modifyPassword")) {
            modifyPassword();
        }
        //找回密码 不显示旧密码
        if (from.equals("retrievePassword")) {
            retrievePassword();
        }
    }

    private void modifyPassword() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.modifyPassword(sp.getString("userId",""), et_modify_new_password.getText().toString().trim(), et_modify_old_password.getText().toString().trim());
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    Log.e("ts", response.body());
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast(ModifyPasswordActivity.this, message);
                        ModifyPasswordActivity.this.finish();
                    } else {
                        ToastUtils.showToast(ModifyPasswordActivity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void retrievePassword() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.retrievePassword(mobile, et_modify_new_password.getText().toString().trim(), et_modify_new_confirmpassword.getText().toString().trim());
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {

                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast(ModifyPasswordActivity.this, message);
                        ModifyPasswordActivity.this.finish();
                    } else {
                        ToastUtils.showToast(ModifyPasswordActivity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
