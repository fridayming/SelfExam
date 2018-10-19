package com.jsxl.selfexam.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.database.DBHelper;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.DrawableTextView;
import com.jsxl.selfexam.widget.ScoreView;

import static android.R.attr.type;

public class TestReportActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private DrawableTextView tv_report_question;
    private DrawableTextView tv_report_time;
    private ScoreView sv_report_number;
    private RelativeLayout rl_title_bar;
    private RelativeLayout rl_report_bg;
    private TextView tv_report_text, tv_report_notice;

    private RelativeLayout rl_report_error,rl_report_redo;
    //统计做题 做对多少 做错多少
    private TextView tv_report_statistics;
    private int count,percent,wrong,right;
    private String time;
    private String chapterName,subjectName;
    private int chapterId,subjectId,chapterPaperType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_report);
        Intent intent = getIntent();
        count = intent.getIntExtra("count",0);
        percent =intent .getIntExtra("percent", 0);
        wrong = intent.getIntExtra("wrong",0);
        right = intent.getIntExtra("right",0);
        time = intent.getStringExtra("time");
        chapterName = intent.getStringExtra("chapterName");
        chapterId = intent.getIntExtra("chapterId",0);
        subjectName = intent.getStringExtra("subjectName");
        subjectId = intent.getIntExtra("subjectId",0);
        chapterPaperType = intent.getIntExtra("chapterPaperType",0);
//        intent.putExtra("time","20:39");
//        intent.putExtra("wrong",20);
//        intent.putExtra("right",35);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_title_bar = (RelativeLayout) findViewById(R.id.title);
        tv_report_question = (DrawableTextView) findViewById(R.id.tv_report_question);
        rl_report_bg = (RelativeLayout) findViewById(R.id.rl_report_bg);
        tv_report_time = (DrawableTextView) findViewById(R.id.tv_report_time);
        sv_report_number = (ScoreView) findViewById(R.id.sv_report_number);
        tv_report_text = (TextView) findViewById(R.id.tv_report_text);
        tv_report_notice = (TextView) findViewById(R.id.tv_report_notice);
        rl_report_error = (RelativeLayout) findViewById(R.id.rl_report_error);
        rl_report_error.setOnClickListener(this);
        rl_report_redo = (RelativeLayout) findViewById(R.id.rl_report_redo);
        rl_report_redo.setOnClickListener(this);
        tv_report_statistics = (TextView) findViewById(R.id.tv_report_statistics);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("测试报告");
        sv_report_number.setCount(percent);
        tv_report_question.setText("题目"+count);
        tv_report_time.setText("用时"+time);
        //填写统计数据
        tv_report_statistics.setText("做对"+right+"题，做错"+wrong+"题");
        if (percent < 60) {
            setStatusColor("#eb5545");
            rl_title_bar.setBackgroundColor(Color.parseColor("#eb5545"));
            rl_report_bg.setBackgroundResource(R.drawable.bg_score0);
            tv_report_text.setText("此次测评成绩一般，继续加油");
            tv_report_notice.setTextColor(Color.parseColor("#eb5545"));
        }
        if (percent >= 60 && percent < 80) {
            setStatusColor("#5cb44f");
            rl_title_bar.setBackgroundColor(Color.parseColor("#5cb44f"));
            rl_report_bg.setBackgroundResource(R.drawable.bg_score1);
            tv_report_text.setText("此次测评成绩合格");
            tv_report_notice.setTextColor(Color.parseColor("#5cb44f"));
        }
        if (percent >= 80 && percent <= 100) {
            setStatusColor("#4397f7");
            rl_title_bar.setBackgroundColor(Color.parseColor("#4397f7"));
            rl_report_bg.setBackgroundResource(R.drawable.bg_score2);
            tv_report_text.setText("此次测评成绩非常优秀");
            tv_report_notice.setTextColor(Color.parseColor("#4397f7"));

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_report_error:
                intent = new Intent(TestReportActivity.this, CollectQuestionActivity.class);
                intent.putExtra("chapterName", chapterName);
                intent.putExtra("chapterId", chapterId);
                intent.putExtra("type", "error");
                startActivity(intent);
                finish();
                break;
            case R.id.rl_report_redo:
                DBHelper dbHelper = new DBHelper(this);
                dbHelper.delQuestionByChapterId(chapterId);
                intent = new Intent(TestReportActivity.this, QuestionActivity.class);
                intent.putExtra("chapterName",chapterName);
                intent.putExtra("chapterId",chapterId);
                intent.putExtra("subjectId",subjectId);
                intent.putExtra("chapterPaperType",chapterPaperType);
                intent.putExtra("subjectName",subjectName);
                intent.putExtra("from","report");
//                chapterName = intent.getStringExtra("chapterName");
//                chapterId = intent.getIntExtra("chapterId", 0);
//                subjectId = intent.getIntExtra("subjectId", 0);
//                chapterPaperType = intent.getIntExtra("chapterPaperType", 0);
//                subjectName = intent.getStringExtra("subjectName");
                startActivity(intent);
                finish();
                break;
        }
    }
}
