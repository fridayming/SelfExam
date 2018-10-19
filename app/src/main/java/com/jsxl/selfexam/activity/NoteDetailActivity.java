package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.adapter.CollectDetailListAdapter;
import com.jsxl.selfexam.adapter.NoteDetailAdapter;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.CollectSecondListEntity;
import com.jsxl.selfexam.bean.NoteEntity;
import com.jsxl.selfexam.interfaces.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.id;
import static com.jsxl.selfexam.R.id.collect_detail_listView;

public class NoteDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_title;
    private ListView note_listview;
    private TextView tv_note_question_number,tv_note_chapter_name;
    private String chapterName;
    private int chapterId,chapterPaperType;
    private SharedPreferences sp;
    private NoteEntity entity;
    private NoteDetailAdapter adapter;
    private RelativeLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        chapterName = getIntent().getStringExtra("chapterName");
        chapterId = getIntent().getIntExtra("chapterId",0);
        chapterPaperType = getIntent().getIntExtra("chapterPaperType",0);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        note_listview = (ListView) findViewById(R.id.note_listview);
        tv_note_chapter_name = (TextView) findViewById(R.id.tv_note_chapter_name);
        tv_note_question_number = (TextView) findViewById(R.id.tv_note_question_number);
        loading = (RelativeLayout) findViewById(R.id.loading);

    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("笔记-"+chapterName);
        tv_note_chapter_name.setText((chapterPaperType==0?"章节练习":"模拟试卷")+"-"+chapterName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNote();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void getNote() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<NoteEntity> call = retrofitService.getNoteQuestion(sp.getString("userId",""),chapterId+"");
        call.enqueue(new Callback<NoteEntity>() {
            @Override
            public void onResponse(Call<NoteEntity> call, Response<NoteEntity> response) {
                //测试数据返回
                entity = response.body();
                adapter = new NoteDetailAdapter(NoteDetailActivity.this, entity);
                note_listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tv_note_question_number.setText("共"+entity.getListnote().size()+"题");
                tv_note_question_number.setVisibility(View.VISIBLE);
                tv_note_chapter_name.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NoteEntity> call, Throwable t) {
                Toast.makeText(NoteDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
