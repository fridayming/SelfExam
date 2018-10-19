package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.adapter.QuestionFragmentPagerAdapter;
import com.jsxl.selfexam.base.BaseFragmentActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.QuestionEntity;
import com.jsxl.selfexam.bean.QuestionScoreEntity;
import com.jsxl.selfexam.database.DBHelper;
import com.jsxl.selfexam.fragment.QuestionAnswerFragment;
import com.jsxl.selfexam.fragment.QuestionCheckBoxQuestionFragment;
import com.jsxl.selfexam.fragment.QuestionClozeTestFragment;
import com.jsxl.selfexam.fragment.QuestionComprehensionFragment;
import com.jsxl.selfexam.fragment.QuestionComprehensionSelectFragment;
import com.jsxl.selfexam.fragment.QuestionRadioQuestionFragment;
import com.jsxl.selfexam.interfaces.AnswerJudgeListener;
import com.jsxl.selfexam.interfaces.QuestionNextOrLastClickListener;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.CustomJJDialog;
import com.jsxl.selfexam.widget.EditNoteDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CollectQuestionActivity extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener, QuestionNextOrLastClickListener, AnswerJudgeListener {
    private ImageView iv_back;
    private TextView tv_title;
    private CheckBox cb_answer, cb_sheet, cb_collect, cb_note, cb_submit;
    private RelativeLayout loading;
    private SharedPreferences sp;
    private String chapterName;
    private Intent intent;
    private ViewPager vp_container;
    private List<Fragment> fragmentList;
    private QuestionFragmentPagerAdapter adapter;
    private TextView tv_question_type, tv_question_number;
    private QuestionEntity entity;
    private int questionNumber = 0;
    private CustomJJDialog dialog;
    private int chapterId;
    //用来记录答案是否显示的map  键为当前的位置  值为false 或 true
    private Map<Integer, Boolean> answerIsShow;
    //键用来记录1单选 2多选 3简答题 4完型 5阅读型问答 6阅读型单选   值为题目数量用来传递给答题卡
    private Map<Integer, Integer> questionTypeNumber;
    //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功
    private Map<Integer, Integer> answerJudge;
    //用来判断是否被收藏 初始化的时候需要同时初始化  1表示收藏 0表示为收藏 null表示未登录
    private Map<Integer, String> collectJudge;
    //储存当前题目的状态 做对做错还是没做等
    private Integer currentAnswerJudge = 0, currentAnswerItem = 0;
    private boolean isTest;
    private DBHelper dbHelper;
    private HashMap<Integer, QuestionScoreEntity> questionScoreEntityMap;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_question);
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        //设置后不会让relativelayout中的alignbottom的布局在弹出输入框时顶上去
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setStatusColor();
        dbHelper = new DBHelper(this);
        intent = getIntent();
        chapterName = intent.getStringExtra("chapterName");
        chapterId = intent.getIntExtra("chapterId", 0);
        type = intent.getStringExtra("type");
        questionScoreEntityMap = dbHelper.getQuestionByChapterId(chapterId);
        initView();
        initData();
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        loading = (RelativeLayout) findViewById(R.id.loading);
        cb_answer = (CheckBox) findViewById(R.id.cb_answer);
        cb_answer.setOnCheckedChangeListener(this);
        cb_sheet = (CheckBox) findViewById(R.id.cb_sheet);
        cb_sheet.setOnCheckedChangeListener(this);
        cb_collect = (CheckBox) findViewById(R.id.cb_collect);
        cb_collect.setOnCheckedChangeListener(this);
        //收藏用onClick不用onCheckChange原因就是 在页面切换的时候会频繁调用onCheckChange 而我们需要的只是onClick事件监听
        cb_collect.setOnClickListener(this);
        cb_note = (CheckBox) findViewById(R.id.cb_note);
        cb_note.setOnCheckedChangeListener(this);
        cb_submit = (CheckBox) findViewById(R.id.cb_submit);
        cb_submit.setOnCheckedChangeListener(this);
        vp_container = (ViewPager) findViewById(R.id.vp_container);
        vp_container.setOnPageChangeListener(this);
        tv_question_type = (TextView) findViewById(R.id.tv_question_type);
        tv_question_number = (TextView) findViewById(R.id.tv_question_number);

    }

    @Override
    public void initData() {
        //是否为测试模式 临时初始
        isTest = false;
        tv_title.setText(chapterName);
        getData();
        answerIsShow = new HashMap<>();
        fragmentList = new ArrayList<>();
        questionTypeNumber = new HashMap<>();
        answerJudge = new HashMap<>();
        collectJudge = new HashMap<>();
        adapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_container.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cb_collect:
                if (cb_collect.isChecked()) {
                    addCollect(getQuestionId());
                } else {
                    delCollect(getQuestionId());
                }
                break;

        }
    }



    /**
     * 获取题目内容
     */
    private void getData() {
        cb_answer.setClickable(false);
        cb_sheet.setClickable(false);
        cb_collect.setClickable(false);
        cb_note.setClickable(false);
        cb_submit.setClickable(false);
        if ("collect".equals(type)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl5)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<QuestionEntity> call = retrofitService.getCollectQuestion(sp.getString("userId", ""), chapterId + "");
            call.enqueue(new Callback<QuestionEntity>() {
                @Override
                public void onResponse(Call<QuestionEntity> call, Response<QuestionEntity> response) {
                    //测试数据返回
                    entity = response.body();
                    questionNumber = entity.getQuestion1s().size()+entity.getQuestion2s().size() + entity.getQuestion4s().size() + entity.getQuestion6s().size() + entity.getQuestion5s().size() + entity.getQuestion3s().size();
                    //键用来记录1单选 2多选 3简答题 4完型 5阅读型问答 6阅读型单选   值为题目数量用来传递给答题卡
                    questionTypeNumber.put(1, entity.getQuestion1s().size());
                    questionTypeNumber.put(2, entity.getQuestion2s().size());
                    questionTypeNumber.put(4, entity.getQuestion4s().size());
                    questionTypeNumber.put(6, entity.getQuestion6s().size());
                    questionTypeNumber.put(5, entity.getQuestion5s().size());
                    questionTypeNumber.put(3, entity.getQuestion3s().size());
                    for (int i = 0; i < entity.getQuestion2s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion2s().get(i).getIsCollect());
                        QuestionCheckBoxQuestionFragment checkBoxFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion2s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion2s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion2s().get(i).getId()).getAnswerJudge());
                            checkBoxFragment = new QuestionCheckBoxQuestionFragment(entity.getQuestion2s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            checkBoxFragment = new QuestionCheckBoxQuestionFragment(entity.getQuestion2s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //传入 fragmentList.size() ，目的是获取到当前fragment在list中的位置 在上一题 下一题使用  如果传入0  则上一题变黑 传入questionNumber-1 则下一题不显示
                        //填充answerIsShow
                        fragmentList.add(checkBoxFragment);

                    }
                    for (int i = 0; i < entity.getQuestion1s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion1s().get(i).getIsCollect());
                        QuestionRadioQuestionFragment radioFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion1s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion1s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion1s().get(i).getId()).getAnswerJudge());
                            radioFragment = new QuestionRadioQuestionFragment(entity.getQuestion1s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            radioFragment = new QuestionRadioQuestionFragment(entity.getQuestion1s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //传入 fragmentList.size() ，目的是获取到当前fragment在list中的位置 在上一题 下一题使用  如果传入0  则上一题变黑 传入questionNumber-1 则下一题不显示
                        //填充answerIsShow
                        fragmentList.add(radioFragment);

                    }
                    for (int i = 0; i < entity.getQuestion4s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion4s().get(i).getIsCollect());

                        QuestionClozeTestFragment clozeFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion4s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion4s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion4s().get(i).getId()).getAnswerJudge());
                            clozeFragment = new QuestionClozeTestFragment(entity.getQuestion4s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            clozeFragment = new QuestionClozeTestFragment(entity.getQuestion4s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //填充answerIsShow
                        fragmentList.add(clozeFragment);

                    }
                    for (int i = 0; i < entity.getQuestion6s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion6s().get(i).getIsCollect());
                        QuestionComprehensionSelectFragment comprehensionSelectFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion6s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion6s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion6s().get(i).getId()).getAnswerJudge());
                            comprehensionSelectFragment = new QuestionComprehensionSelectFragment(entity.getQuestion6s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            comprehensionSelectFragment = new QuestionComprehensionSelectFragment(entity.getQuestion6s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //填充answerIsShow
                        fragmentList.add(comprehensionSelectFragment);

                    }
                    for (int i = 0; i < entity.getQuestion5s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion5s().get(i).getIsCollect());
                        QuestionComprehensionFragment comprehensionFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion5s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion5s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion5s().get(i).getId()).getAnswerJudge());
                            comprehensionFragment = new QuestionComprehensionFragment(entity.getQuestion5s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            comprehensionFragment = new QuestionComprehensionFragment(entity.getQuestion5s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, "");
                        }//填充answerIsShow
                        fragmentList.add(comprehensionFragment);

                    }
                    for (int i = 0; i < entity.getQuestion3s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion3s().get(i).getIsCollect());
                        QuestionAnswerFragment answerFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion3s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion3s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion3s().get(i).getId()).getAnswerJudge());
                            answerFragment = new QuestionAnswerFragment(entity.getQuestion3s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            answerFragment = new QuestionAnswerFragment(entity.getQuestion3s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, "");
                        }
                        fragmentList.add(answerFragment);
                    }
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    if (questionNumber > 0) {
                        tv_question_number.setText("1/" + questionNumber);
                        cb_answer.setClickable(true);
                        cb_sheet.setClickable(true);
                        cb_collect.setClickable(true);
                        cb_note.setClickable(true);
                        //初始化收藏  在请求成功初始第一题收藏 和页面切换时修改收藏状态
                        cb_collect.setChecked("1".equals(collectJudge.get(0)));
                        cb_submit.setClickable(true);
                    }

                }

                @Override
                public void onFailure(Call<QuestionEntity> call, Throwable t) {
                    Log.e("ts", "请求失败");
                }
            });
        } else if ("error".equals(type)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl5)//基础URL 建议以 / 结尾
                    .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<QuestionEntity> call = retrofitService.getErrorQuestion(sp.getString("userId", ""), chapterId + "");
            call.enqueue(new Callback<QuestionEntity>() {
                @Override
                public void onResponse(Call<QuestionEntity> call, Response<QuestionEntity> response) {
                    //测试数据返回
                    entity = response.body();
                    questionNumber =entity.getQuestion2s().size() + entity.getQuestion1s().size() + entity.getQuestion4s().size() + entity.getQuestion6s().size() + entity.getQuestion5s().size() + entity.getQuestion3s().size();
                    //键用来记录1单选 2多选 3简答题 4完型 5阅读型问答 6阅读型单选   值为题目数量用来传递给答题卡
                    questionTypeNumber.put(1, entity.getQuestion1s().size());
                    questionTypeNumber.put(2, entity.getQuestion2s().size());
                    questionTypeNumber.put(4, entity.getQuestion4s().size());
                    questionTypeNumber.put(6, entity.getQuestion6s().size());
                    questionTypeNumber.put(5, entity.getQuestion5s().size());
                    questionTypeNumber.put(3, entity.getQuestion3s().size());
                    for (int i = 0; i < entity.getQuestion2s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion2s().get(i).getIsCollect());
                        QuestionCheckBoxQuestionFragment checkBoxFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion2s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion2s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion2s().get(i).getId()).getAnswerJudge());
                            checkBoxFragment = new QuestionCheckBoxQuestionFragment(entity.getQuestion2s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            checkBoxFragment = new QuestionCheckBoxQuestionFragment(entity.getQuestion2s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //传入 fragmentList.size() ，目的是获取到当前fragment在list中的位置 在上一题 下一题使用  如果传入0  则上一题变黑 传入questionNumber-1 则下一题不显示
                        //填充answerIsShow
                        fragmentList.add(checkBoxFragment);

                    }
                    for (int i = 0; i < entity.getQuestion1s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion1s().get(i).getIsCollect());
                        QuestionRadioQuestionFragment radioFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion1s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion1s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion1s().get(i).getId()).getAnswerJudge());
                            radioFragment = new QuestionRadioQuestionFragment(entity.getQuestion1s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            radioFragment = new QuestionRadioQuestionFragment(entity.getQuestion1s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //传入 fragmentList.size() ，目的是获取到当前fragment在list中的位置 在上一题 下一题使用  如果传入0  则上一题变黑 传入questionNumber-1 则下一题不显示
                        //填充answerIsShow
                        fragmentList.add(radioFragment);

                    }
                    for (int i = 0; i < entity.getQuestion4s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion4s().get(i).getIsCollect());

                        QuestionClozeTestFragment clozeFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion4s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion4s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion4s().get(i).getId()).getAnswerJudge());
                            clozeFragment = new QuestionClozeTestFragment(entity.getQuestion4s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            clozeFragment = new QuestionClozeTestFragment(entity.getQuestion4s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //填充answerIsShow
                        fragmentList.add(clozeFragment);

                    }
                    for (int i = 0; i < entity.getQuestion6s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion6s().get(i).getIsCollect());
                        QuestionComprehensionSelectFragment comprehensionSelectFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion6s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion6s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion6s().get(i).getId()).getAnswerJudge());
                            comprehensionSelectFragment = new QuestionComprehensionSelectFragment(entity.getQuestion6s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            comprehensionSelectFragment = new QuestionComprehensionSelectFragment(entity.getQuestion6s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
                        }
                        //填充answerIsShow
                        fragmentList.add(comprehensionSelectFragment);

                    }
                    for (int i = 0; i < entity.getQuestion5s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion5s().get(i).getIsCollect());
                        QuestionComprehensionFragment comprehensionFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion5s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion5s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion5s().get(i).getId()).getAnswerJudge());
                            comprehensionFragment = new QuestionComprehensionFragment(entity.getQuestion5s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            comprehensionFragment = new QuestionComprehensionFragment(entity.getQuestion5s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, "");
                        }//填充answerIsShow
                        fragmentList.add(comprehensionFragment);

                    }
                    for (int i = 0; i < entity.getQuestion3s().size(); i++) {
                        answerIsShow.put(fragmentList.size(), false);
                        collectJudge.put(fragmentList.size(), entity.getQuestion3s().get(i).getIsCollect());
                        QuestionAnswerFragment answerFragment;
                        if (questionScoreEntityMap.get(entity.getQuestion3s().get(i).getId()) != null) {
                            String answer = questionScoreEntityMap.get(entity.getQuestion3s().get(i).getId()).getAnswer();
                            answerJudge.put(fragmentList.size(), questionScoreEntityMap.get(entity.getQuestion3s().get(i).getId()).getAnswerJudge());
                            answerFragment = new QuestionAnswerFragment(entity.getQuestion3s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, answer);

                        } else {
                            answerJudge.put(fragmentList.size(), 0);
                            answerFragment = new QuestionAnswerFragment(entity.getQuestion3s().get(i), CollectQuestionActivity.this, CollectQuestionActivity.this, fragmentList.size(), questionNumber, chapterId, "");
                        }
                        fragmentList.add(answerFragment);
                    }
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);
                    if (questionNumber > 0) {
                        tv_question_number.setText("1/" + questionNumber);
                        cb_answer.setClickable(true);
                        cb_sheet.setClickable(true);
                        cb_collect.setClickable(true);
                        cb_note.setClickable(true);
                        //初始化收藏  在请求成功初始第一题收藏 和页面切换时修改收藏状态
                        cb_collect.setChecked("1".equals(collectJudge.get(0)));
                        cb_submit.setClickable(true);
                    }

                }

                @Override
                public void onFailure(Call<QuestionEntity> call, Throwable t) {
                    Log.e("ts", "请求失败");
                }
            });
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_answer:
                if (isChecked) {
                    cb_answer.setTextColor(Color.parseColor("#4397f7"));
                    answerIsShow.put(vp_container.getCurrentItem(), true);
                    if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionCheckBoxQuestionFragment) {
                        ((QuestionCheckBoxQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(true);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionRadioQuestionFragment) {
                        ((QuestionRadioQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(true);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionClozeTestFragment) {
                        ((QuestionClozeTestFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(true);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionSelectFragment) {
                        ((QuestionComprehensionSelectFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(true);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionFragment) {
                        ((QuestionComprehensionFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(true);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionAnswerFragment) {
                        ((QuestionAnswerFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(true);
                    }
                } else {
                    cb_answer.setTextColor(Color.parseColor("#333333"));
                    answerIsShow.put(vp_container.getCurrentItem(), false);
                    if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionCheckBoxQuestionFragment) {
                        ((QuestionCheckBoxQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(false);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionRadioQuestionFragment) {
                        ((QuestionRadioQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(false);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionClozeTestFragment) {
                        ((QuestionClozeTestFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(false);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionSelectFragment) {
                        ((QuestionComprehensionSelectFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(false);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionFragment) {
                        ((QuestionComprehensionFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(false);
                    } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionAnswerFragment) {
                        ((QuestionAnswerFragment) fragmentList.get(vp_container.getCurrentItem())).showAnalysis(false);
                    }
                }
                break;
            case R.id.cb_sheet:
                if (isChecked) {
                    cb_sheet.setTextColor(Color.parseColor("#4397f7"));
                    showSheet();
                } else {
                    cb_sheet.setTextColor(Color.parseColor("#333333"));
                }
                break;
            case R.id.cb_collect:
                if (isChecked) {
                    cb_collect.setTextColor(Color.parseColor("#4397f7"));
                } else {
                    cb_collect.setTextColor(Color.parseColor("#333333"));
                }
                break;
            case R.id.cb_note:
                if (isChecked) {
                    cb_note.setTextColor(Color.parseColor("#4397f7"));
                    checkNoteExist();
                } else {
                    cb_note.setTextColor(Color.parseColor("#333333"));
                }
                break;
            case R.id.cb_submit:
                if (isChecked) {
                    ToastUtils.showToast(this, "该模式不支持交卷");
                    cb_submit.setChecked(false);
                } else {
                }
                break;
        }

    }

    private void checkNoteExist() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.queryNoteExist(sp.getString("userId", ""), getQuestionId() + "");
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    if (success) {
                        boolean isExist = false;
                        JSONObject jsUser = js.getJSONObject("userNote");
                        String noteContent = jsUser.getString("noteContent");
                        String noteId = jsUser.getString("id");
                        if ((!"".equals(noteContent)) && noteContent != null && (!"null".equals(noteContent))) {
                            isExist = true;
                        }
                        showEditNoteDialog(isExist, noteContent, noteId);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void showEditNoteDialog(final boolean exist, String content, final String noteId) {
        final EditNoteDialog dialog = new EditNoteDialog(this);
        if (exist) {
            dialog.setMessage(content);
        }
        dialog.setYesOnclickListener(new EditNoteDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String noteContent) {
                if (noteContent == null || "".equals(noteContent)) {
                    ToastUtils.showToast(CollectQuestionActivity.this, "笔记不能为空");
                } else {
                    addNote(exist, noteContent, noteId);
                    dialog.dismiss();
                }
            }
        });
        dialog.setNoOnclickListener(new EditNoteDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });//dialog消失时笔记onCheck设置false
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cb_note.setChecked(false);
            }
        });
        dialog.show();
    }

    private void addNote(boolean isExist, String noteContent, String noteId) {
        if (isExist) {
            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
            Call<String> call2 = retrofitService2.updateNote(sp.getString("userId", ""), noteId, noteContent);
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //测试数据返回
                    try {
                        JSONObject js = new JSONObject(response.body());
                        boolean success = js.getBoolean("success");
                        String message = js.getString("message");
                        if (success) {
                            ToastUtils.showToast(CollectQuestionActivity.this, "修改成功");
                            cb_note.setChecked(false);
                        } else {
                            ToastUtils.showToast(CollectQuestionActivity.this, "修改失败");
                            cb_note.setChecked(false);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        } else {
            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                    .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                    .build();
            RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
            Call<String> call2 = retrofitService2.addNote(sp.getString("userId", ""), noteContent, chapterId + "", getQuestionId() + "");
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //测试数据返回
                    try {
                        JSONObject js = new JSONObject(response.body());
                        boolean success = js.getBoolean("success");
                        String message = js.getString("message");
                        if (success) {
                            ToastUtils.showToast(CollectQuestionActivity.this, "添加成功");
                            cb_note.setChecked(false);
                        } else {
                            ToastUtils.showToast(CollectQuestionActivity.this, "添加失败");
                            cb_note.setChecked(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }
    }

    private void showSheet() {
        //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功
        int right = 0;
        int wrong = 0;
        int answer = 0;
        for (int key : answerJudge.keySet()) {
            int value = answerJudge.get(key);
            if (value == 1) {
                right += 1;
                answer += 1;
            }
            if (value == 2) {
                wrong += 1;
                answer += 1;
            }
            if (value == 4) {
                answer += 1;
            }
        }
        currentAnswerItem = vp_container.getCurrentItem();
        currentAnswerJudge = answerJudge.get(vp_container.getCurrentItem());
        answerJudge.put(vp_container.getCurrentItem(), 3);
        Intent intent = new Intent(this, QuestionCardDialogActivity.class);
        intent.putExtra("answerJudge", (Serializable) answerJudge);
        intent.putExtra("questionTypeNumber", (Serializable) questionTypeNumber);
        intent.putExtra("currentItem", vp_container.getCurrentItem());
        intent.putExtra("right", right);
        intent.putExtra("wrong", wrong);
        intent.putExtra("answer", answer);
        intent.putExtra("allQuestion", answerJudge.size());
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
    }

    /**
     * 添加收藏
     *
     * @param questionId
     */
    private void addCollect(int questionId) {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.addCollect(sp.getString("userId", ""), chapterId + "", questionId + "");
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast(CollectQuestionActivity.this, message);
                        collectJudge.put(vp_container.getCurrentItem(), "1");
                    } else {
                        ToastUtils.showToast(CollectQuestionActivity.this, message);
                        cb_collect.setChecked(false);
                        collectJudge.put(vp_container.getCurrentItem(), "0");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param questionId
     */
    private void delCollect(int questionId) {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.delCollect(sp.getString("userId", ""), chapterId + "", questionId + "");
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast(CollectQuestionActivity.this, message);
                        //将map传递给答题卡的时候 需要在删除后修改map数据
                        if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionCheckBoxQuestionFragment) {
                            questionTypeNumber.put(2, questionTypeNumber.get(2) - 1);
                        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionRadioQuestionFragment) {
                            questionTypeNumber.put(1, questionTypeNumber.get(1) - 1);
                        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionClozeTestFragment) {
                            //4
                            questionTypeNumber.put(4, questionTypeNumber.get(4) - 1);
                        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionSelectFragment) {
                            //6
                            questionTypeNumber.put(6, questionTypeNumber.get(6) - 1);
                        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionFragment) {
                            //5
                            questionTypeNumber.put(5, questionTypeNumber.get(5) - 1);
                        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionAnswerFragment) {
                            //3
                            questionTypeNumber.put(3, questionTypeNumber.get(3) - 1);
                        }
                        fragmentList.remove(vp_container.getCurrentItem());
                        //如果剩余为0 则显示0/0 如果不为0则显示m/n
                        if (fragmentList.size() == 0) {
                            tv_question_number.setText("0/0");
                        } else {
                            tv_question_number.setText((vp_container.getCurrentItem() + 1) + "/" + fragmentList.size() + "");
                        }
                        adapter.notifyDataSetChanged();
                        cb_collect.setChecked(true);
                    } else {
                        ToastUtils.showToast(CollectQuestionActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tv_question_number.setText((position + 1) + "/" + fragmentList.size() + "");
        cb_collect.setChecked("1".equals(collectJudge.get(position)) ? true : false);
        if (fragmentList.get(position) instanceof QuestionCheckBoxQuestionFragment) {
            tv_question_type.setText("多选题");
        } else if (fragmentList.get(position) instanceof QuestionRadioQuestionFragment) {
            tv_question_type.setText("单选题");
        } else if (fragmentList.get(position) instanceof QuestionClozeTestFragment) {
            tv_question_type.setText("完形填空");
        } else if (fragmentList.get(position) instanceof QuestionComprehensionSelectFragment) {
            tv_question_type.setText("阅读理解");
        } else if (fragmentList.get(position) instanceof QuestionComprehensionFragment) {
            tv_question_type.setText("阅读理解");
        } else if (fragmentList.get(position) instanceof QuestionAnswerFragment) {
            tv_question_type.setText("简答题");
        }
         if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionCheckBoxQuestionFragment) {
            cb_answer.setChecked(answerIsShow.get(position));
                QuestionCheckBoxQuestionFragment fragment = (QuestionCheckBoxQuestionFragment) fragmentList.get(vp_container.getCurrentItem());
            fragment.showAnalysis(answerIsShow.get(position));
//            cb_collect.setChecked("1".equals(fragment.getCollect())?true:false);
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionRadioQuestionFragment) {
            cb_answer.setChecked(answerIsShow.get(position));
            QuestionRadioQuestionFragment fragment = (QuestionRadioQuestionFragment) fragmentList.get(vp_container.getCurrentItem());
            fragment.showAnalysis(answerIsShow.get(position));
//            cb_collect.setChecked("1".equals(fragment.getCollect())?true:false);
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionClozeTestFragment) {
            cb_answer.setChecked(answerIsShow.get(position));
            QuestionClozeTestFragment fragment = (QuestionClozeTestFragment) fragmentList.get(vp_container.getCurrentItem());
            fragment.showAnalysis(answerIsShow.get(position));
//            cb_collect.setChecked("1".equals(fragment.getCollect())?true:false);
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionSelectFragment) {
            cb_answer.setChecked(answerIsShow.get(position));
            QuestionComprehensionSelectFragment fragment = (QuestionComprehensionSelectFragment) fragmentList.get(vp_container.getCurrentItem());
            fragment.showAnalysis(answerIsShow.get(position));
//            cb_collect.setChecked("1".equals(fragment.getCollect())?true:false);
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionFragment) {
            cb_answer.setChecked(answerIsShow.get(position));
            QuestionComprehensionFragment fragment = (QuestionComprehensionFragment) fragmentList.get(vp_container.getCurrentItem());
            fragment.showAnalysis(answerIsShow.get(position));
//            cb_collect.setChecked("1".equals(fragment.getCollect())?true:false);
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionAnswerFragment) {
            cb_answer.setChecked(answerIsShow.get(position));
            QuestionAnswerFragment fragment = (QuestionAnswerFragment) fragmentList.get(vp_container.getCurrentItem());
            fragment.showAnalysis(answerIsShow.get(position));
//            cb_collect.setChecked("1".equals(fragment.getCollect())?true:false);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 对fragment中的上一题 下一题进行监听  通过接口回掉传递给Activity并执行相应的操作
     *
     * @param type == 0 表示上一题 1表示下一题
     */
    @Override
    public void nextOrLast(int type) {
        int now;
        if (type == 0) {
            now = vp_container.getCurrentItem();
            vp_container.setCurrentItem(now - 1);
        } else if (type == 1) {
            now = vp_container.getCurrentItem();
            vp_container.setCurrentItem(now + 1);
        }
    }

    /**
     * 将fragment中对对错的判断通过接口回掉形式传递给当前Activity
     *
     * @param type 用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功
     */
    @Override
    public void answerJudge(int type) {
        answerJudge.put(vp_container.getCurrentItem(), type);
    }

    /**
     * 获取questionId  添加删除收藏 笔记时候使用
     *
     * @return
     */
    public int getQuestionId() {
        int questionId = 0;
        if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionCheckBoxQuestionFragment) {
            questionId = ((QuestionCheckBoxQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).getQuestionId();
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionAnswerFragment) {
            questionId = ((QuestionAnswerFragment) fragmentList.get(vp_container.getCurrentItem())).getQuestionId();
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionClozeTestFragment) {
            questionId = ((QuestionClozeTestFragment) fragmentList.get(vp_container.getCurrentItem())).getQuestionId();
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionFragment) {
            questionId = ((QuestionComprehensionFragment) fragmentList.get(vp_container.getCurrentItem())).getQuestionId();
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionComprehensionSelectFragment) {
            questionId = ((QuestionComprehensionSelectFragment) fragmentList.get(vp_container.getCurrentItem())).getQuestionId();
        } else if (fragmentList.get(vp_container.getCurrentItem()) instanceof QuestionRadioQuestionFragment) {
            questionId = ((QuestionRadioQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).getQuestionId();
        }
        return questionId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            cb_sheet.setChecked(false);
            int jumpPosition = data.getIntExtra("jumpPosition", 0);
            vp_container.setCurrentItem(jumpPosition);
            answerJudge.put(currentAnswerItem, currentAnswerJudge);
        }
    }
}
