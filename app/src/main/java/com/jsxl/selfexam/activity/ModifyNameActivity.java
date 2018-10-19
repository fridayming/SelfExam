package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ModifyNameActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_modify_save;
    private EditText et_modify_name;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
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
        tv_modify_save = (TextView) findViewById(R.id.tv_modify_save);
        tv_modify_save.setOnClickListener(this);
        et_modify_name = (EditText) findViewById(R.id.et_modify_name);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("修改昵称");
        et_modify_name.setText(sp.getString("userName",""));
        et_modify_name.setSelection(sp.getString("userName","").length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_modify_save:
                if(et_modify_name.getText().toString().trim().length()>20){
                    ToastUtils.showToast(this,"请不要超过20字");
                    return;
                }
                if(!checkName(et_modify_name.getText().toString().trim())){
                    ToastUtils.showToast(this,"昵称只能包含中英文及数字");
                    return;
                }
                saveUserName();
                break;
        }

    }
    private boolean checkName(String content){
        String regex="^[a-zA-Z0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match=pattern.matcher(content);
       return match.matches();
    }
    private void saveUserName() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit.create(RetrofitService.class);
        Call<String> call = retrofitService2.updateUserInfo(sp.getString("userId",""), et_modify_name.getText().toString().trim(), sp.getInt("gender",0)+"",sp.getString("age","0"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    Log.e("ts",response.body());
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        SharedPreferences.Editor et = sp.edit();
                        et.putString("userName",et_modify_name.getText().toString().trim());
                        et.apply();
                        ToastUtils.showToast(ModifyNameActivity.this,"修改成功");
                        ModifyNameActivity.this.finish();
                    } else {
                        ToastUtils.showToast(ModifyNameActivity.this, message);
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
