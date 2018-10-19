package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.adapter.SystemMessageAdapter;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.SystemMessageEntity;
import com.jsxl.selfexam.interfaces.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SystemMessageActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_title;
    private ListView sys_listview;
    private SystemMessageAdapter adapter;
    private SharedPreferences sp;
    private RelativeLayout rl_empty;
    private RelativeLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
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
        sys_listview = (ListView) findViewById(R.id.sys_listview);
        rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
        loading = (RelativeLayout) findViewById(R.id.loading);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("系统消息");
        getData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl3)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<SystemMessageEntity> call = retrofitService.getSystemMessage(sp.getString("userId",""));
        call.enqueue(new Callback<SystemMessageEntity>() {
            @Override
            public void onResponse(Call<SystemMessageEntity> call, Response<SystemMessageEntity> response) {
                //测试数据返回
                SystemMessageEntity systemMessageEntity = response.body();
                adapter = new SystemMessageAdapter(SystemMessageActivity.this, systemMessageEntity);
                sys_listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
                if(systemMessageEntity.getListSystemMessge().size()==0){
                    rl_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SystemMessageEntity> call, Throwable t) {
                Toast.makeText(SystemMessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                rl_empty.setVisibility(View.VISIBLE);
            }
        });
    }
}
