package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.adapter.ChapterListAdapter;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.ChapterEntity;
import com.jsxl.selfexam.database.DBHelper;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.widget.DrawableTextView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChapterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private RelativeLayout loading;
    private ListView lv_chapter;
    private DrawableTextView tv_chapter_title;
    private ChapterListAdapter adapter;
    private View chapter_line;
    private ChapterEntity chapterEntity;
    private int subjectId;
    private int chapterPaperType;
    private String subjectName;
    private SharedPreferences sp;
    private DBHelper dbHelper;
    private HashMap<Integer, Integer> questionNumByChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        setStatusColor();
        subjectId = getIntent().getIntExtra("subjectId", 0);
        chapterPaperType = getIntent().getIntExtra("chapterPaperType", 0);
        subjectName = getIntent().getStringExtra("subjectName");
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        dbHelper = new DBHelper(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        loading = (RelativeLayout) findViewById(R.id.loading);
        lv_chapter = (ListView) findViewById(R.id.lv_chapter);
        chapter_line = findViewById(R.id.chapter_line);
        tv_chapter_title = (DrawableTextView) findViewById(R.id.tv_chapter_title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionNumByChapter = dbHelper.getQuestionNumByChapterId();
        getData();
    }

    @Override
    public void initData() {
        super.initData();
        tv_chapter_title.setText(subjectName + "（" + sp.getString("examName", "专升本") + "）");
        //初始化验证码
        if (chapterPaperType == 0) {
            tv_title.setText("章节练习");
        } else {
            tv_title.setText("模拟试卷");
        }
        lv_chapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //全做完则删除sql该条数据 重做
                if (chapterEntity.getEntity().get(position).getQuestionNum() == chapterEntity.getEntity().get(position).getFinishNum()) {
                    dbHelper.delQuestionByChapterId(chapterEntity.getEntity().get(position).getId());
                }
                Intent intent = new Intent(ChapterActivity.this, QuestionActivity.class);
                intent.putExtra("chapterName", chapterEntity.getEntity().get(position).getChapterPaperName());
                intent.putExtra("chapterId", chapterEntity.getEntity().get(position).getId());
                intent.putExtra("chapterPaperType", chapterPaperType);
                intent.putExtra("subjectId", subjectId);
                intent.putExtra("subjectName", subjectName);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ChapterEntity> call = retrofitService.getChapter(subjectId + "", chapterPaperType + "");
        call.enqueue(new Callback<ChapterEntity>() {
            @Override
            public void onResponse(Call<ChapterEntity> call, Response<ChapterEntity> response) {
                //测试数据返回
                chapterEntity = response.body();
                for (int i = 0; i < chapterEntity.getEntity().size(); i++) {
                    if (questionNumByChapter.get(chapterEntity.getEntity().get(i).getId()) == null) {
                        chapterEntity.getEntity().get(i).setFinishNum(0);
                    } else {
                        chapterEntity.getEntity().get(i).setFinishNum(questionNumByChapter.get(chapterEntity.getEntity().get(i).getId()));
                    }
                }
                adapter = new ChapterListAdapter(ChapterActivity.this, chapterEntity);
//                ToastUtils.showToast(ChapterActivity.this,chapterEntity.getEntity().get(0).getChapterPaperName());
                lv_chapter.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (chapterEntity.getEntity().size() > 0) {
                    chapter_line.setVisibility(View.VISIBLE);
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ChapterEntity> call, Throwable t) {
                Toast.makeText(ChapterActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });
    }
}
