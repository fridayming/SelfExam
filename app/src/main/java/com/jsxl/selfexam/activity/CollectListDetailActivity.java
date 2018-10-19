package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.adapter.CollectDetailListAdapter;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.CollectSecondListEntity;
import com.jsxl.selfexam.bean.SubjectEntity;
import com.jsxl.selfexam.interfaces.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CollectListDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_title;
    private String name;
    private ListView collect_detail_listView;
    private RelativeLayout loading;
    private CollectSecondListEntity collectSecondListEntity;
    private CollectDetailListAdapter adapter;
    private int id,chapterPaperType;
    private SharedPreferences sp;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list_detail);
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        name = getIntent().getStringExtra("name");
        id = getIntent().getIntExtra("id",0);
        chapterPaperType = getIntent().getIntExtra("chapterPaperType",0);
        type = getIntent().getStringExtra("type");
        initView();
        initData();
    }
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        collect_detail_listView = (ListView) findViewById(R.id.collect_detail_listView);
        loading = (RelativeLayout) findViewById(R.id.loading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void initData() {
        if("error".equals(type)){
            tv_title.setText(name+"-错题");
        }else if("collect".equals(type)){
            tv_title.setText(name+"-收藏");
        }else if("note".equals(type)){
            tv_title.setText(name+"-笔记");
        }
        collect_detail_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("note".equals(type)) {
                    Intent intent = new Intent(CollectListDetailActivity.this, NoteDetailActivity.class);
                    intent.putExtra("chapterName", collectSecondListEntity.getList().get(position).getChapterpapername());
                    intent.putExtra("chapterId", collectSecondListEntity.getList().get(position).getId());
                    intent.putExtra("chapterPaperType",chapterPaperType);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(CollectListDetailActivity.this, CollectQuestionActivity.class);
                    intent.putExtra("chapterName", collectSecondListEntity.getList().get(position).getChapterpapername());
                    intent.putExtra("chapterId", collectSecondListEntity.getList().get(position).getId());
                    intent.putExtra("type", type);
                    startActivity(intent);

                }
            }
        });

    }private void getData() {
        if("collect".equals(type)){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<CollectSecondListEntity> call = retrofitService.getCollectSecondList(sp.getString("userId",""),id+"",chapterPaperType+"");
            call.enqueue(new Callback<CollectSecondListEntity>() {
                @Override
                public void onResponse(Call<CollectSecondListEntity> call, Response<CollectSecondListEntity> response) {
                    //测试数据返回
                    collectSecondListEntity = response.body();
                    adapter = new CollectDetailListAdapter(CollectListDetailActivity.this, collectSecondListEntity);
                    collect_detail_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<CollectSecondListEntity> call, Throwable t) {
                    Toast.makeText(CollectListDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }else if("error".equals(type)){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<CollectSecondListEntity> call = retrofitService.getErrorSecondList(sp.getString("userId",""),id+"",chapterPaperType+"");
            call.enqueue(new Callback<CollectSecondListEntity>() {
                @Override
                public void onResponse(Call<CollectSecondListEntity> call, Response<CollectSecondListEntity> response) {
                    //测试数据返回
                    collectSecondListEntity = response.body();
                    adapter = new CollectDetailListAdapter(CollectListDetailActivity.this, collectSecondListEntity);
                    collect_detail_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<CollectSecondListEntity> call, Throwable t) {
                    Toast.makeText(CollectListDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if("note".equals(type)){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<CollectSecondListEntity> call = retrofitService.getNoteSecondList(sp.getString("userId",""),id+"",chapterPaperType+"");
            call.enqueue(new Callback<CollectSecondListEntity>() {
                @Override
                public void onResponse(Call<CollectSecondListEntity> call, Response<CollectSecondListEntity> response) {
                    //测试数据返回
                    collectSecondListEntity = response.body();
                    adapter = new CollectDetailListAdapter(CollectListDetailActivity.this, collectSecondListEntity);
                    collect_detail_listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<CollectSecondListEntity> call, Throwable t) {
                    Toast.makeText(CollectListDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
