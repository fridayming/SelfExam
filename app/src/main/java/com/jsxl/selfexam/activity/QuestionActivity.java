package com.jsxl.selfexam.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.jsxl.selfexam.fragment.QuestionCheckBoxQuestionFragment;
import com.jsxl.selfexam.fragment.QuestionClozeTestFragment;
import com.jsxl.selfexam.fragment.QuestionComprehensionFragment;
import com.jsxl.selfexam.fragment.QuestionComprehensionSelectFragment;
import com.jsxl.selfexam.fragment.QuestionRadioQuestionFragment;
import com.jsxl.selfexam.fragment.QuestionAnswerFragment;
import com.jsxl.selfexam.interfaces.AnswerJudgeListener;
import com.jsxl.selfexam.interfaces.QuestionNextOrLastClickListener;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.CustomJJDialog;
import com.jsxl.selfexam.widget.EditNoteDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.R.attr.data;
import static android.R.attr.fragment;
import static android.R.attr.password;

public class QuestionActivity extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener, QuestionNextOrLastClickListener, AnswerJudgeListener {
    private ImageView iv_back;
    private TextView tv_title;
    private RelativeLayout loading;
    private TextView tv_question_time;
    private CheckBox cb_answer, cb_sheet, cb_collect, cb_note, cb_submit;
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
    private int chapterId, chapterPaperType, subjectId, progressNow;
    private String from;
    private String subjectName;
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
    // 正确个数 交卷统计使用
    private int correntNumber = 0, uncorrentNumber;
    //unanswerNumber 未答题数量 answerNumber 答题数量 只计算单选 完型 选择型阅读
    private int unanswerNumber = 0, answerNumber = 0;
    private boolean isTest;
    //考试计时
    private Timer timer;
    private TimerTask timerTask;
    private int second = 0;
    private boolean questionLoading = false;
    private DBHelper dbHelper;
    private HashMap<Integer, QuestionScoreEntity> questionScoreEntityMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        //设置后不会让relativelayout中的alignbottom的布局在弹出输入框时顶上去
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dbHelper = new DBHelper(this);
        intent = getIntent();
        chapterName = intent.getStringExtra("chapterName");
        chapterId = intent.getIntExtra("chapterId", 0);
        subjectId = intent.getIntExtra("subjectId", 0);
        chapterPaperType = intent.getIntExtra("chapterPaperType", 0);
        subjectName = intent.getStringExtra("subjectName");
        progressNow = intent.getIntExtra("progressNow", -1);
        from = intent.getStringExtra("from");
        questionScoreEntityMap = dbHelper.getQuestionByChapterId(chapterId);
        if (chapterPaperType == 0) {
            isTest = true;
        } else {
            isTest = false;
        }
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //储存做题进度
        if (questionNumber > 0) {
            SharedPreferences.Editor et = sp.edit();
            et.putInt("progressTotal", questionNumber);
            et.putInt("progressNow", vp_container.getCurrentItem() + 1);
            et.putString("chapterName", chapterName);
            et.putString("subjectName", subjectName);
            et.putInt("chapterId", chapterId);
            et.putInt("subjectId", subjectId);
            et.putInt("chapterPaperType", chapterPaperType);
            et.commit();
        }
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        loading = (RelativeLayout) findViewById(R.id.loading);
        tv_question_time = (TextView) findViewById(R.id.tv_question_time);
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
        tv_title.setText(chapterName);
        //记录跳转不显示时间
        if ("record".equals(from)) {
            tv_question_time.setVisibility(View.GONE);
        }
        getData();
        answerIsShow = new HashMap<>();
        fragmentList = new ArrayList<>();
        questionTypeNumber = new HashMap<>();
        answerJudge = new HashMap<>();
        collectJudge = new HashMap<>();
        adapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp_container.setAdapter(adapter);
        //(int chapterPaperType, int subjectId, int chapterId,String chapterName, String chapterNumber, Long createTime)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if ("record".equals(from)) {
                    finish();
                } else {
                    dialog = new CustomJJDialog(this);
                    dialog.setTitle("提示");
                    dialog.setMessage("是否结束本次测试");
                    dialog.setYesOnclickListener("继续", new CustomJJDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setNoOnclickListener("退出", new CustomJJDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    dialog.show();
                }
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
//        ToastUtils.showToast(this,chapterId+" "+sp.getString("userId", ""));
        cb_answer.setClickable(false);
        cb_sheet.setClickable(false);
        cb_collect.setClickable(false);
        cb_note.setClickable(false);
        cb_submit.setClickable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl5)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<QuestionEntity> call = retrofitService.getQuestion(chapterId + "", sp.getString("userId", ""));
        call.enqueue(new Callback<QuestionEntity>() {
            @Override
            public void onResponse(Call<QuestionEntity> call, Response<QuestionEntity> response) {
                //测试数据返回
                entity = response.body();
                questionNumber = entity.getQuestion1s().size() + entity.getQuestion2s().size() + entity.getQuestion4s().size() + entity.getQuestion6s().size() + entity.getQuestion5s().size() + entity.getQuestion3s().size();
                dbHelper.addRecord(chapterPaperType, subjectId, chapterId, chapterName, questionNumber + "", System.currentTimeMillis());
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
                        checkBoxFragment = new QuestionCheckBoxQuestionFragment(entity.getQuestion2s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                    } else {
                        answerJudge.put(fragmentList.size(), 0);
                        checkBoxFragment = new QuestionCheckBoxQuestionFragment(entity.getQuestion2s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
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
                        Log.e(fragmentList.size() + "", questionScoreEntityMap.get(entity.getQuestion1s().get(i).getId()).getAnswerJudge() + "");
                        radioFragment = new QuestionRadioQuestionFragment(entity.getQuestion1s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                    } else {
                        answerJudge.put(fragmentList.size(), 0);
                        radioFragment = new QuestionRadioQuestionFragment(entity.getQuestion1s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
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
                        clozeFragment = new QuestionClozeTestFragment(entity.getQuestion4s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                    } else {
                        answerJudge.put(fragmentList.size(), 0);
                        clozeFragment = new QuestionClozeTestFragment(entity.getQuestion4s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
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
                        comprehensionSelectFragment = new QuestionComprehensionSelectFragment(entity.getQuestion6s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, answer);

                    } else {
                        answerJudge.put(fragmentList.size(), 0);
                        comprehensionSelectFragment = new QuestionComprehensionSelectFragment(entity.getQuestion6s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, isTest, chapterId, "");
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
                        comprehensionFragment = new QuestionComprehensionFragment(entity.getQuestion5s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, chapterId, answer);

                    } else {
                        answerJudge.put(fragmentList.size(), 0);
                        comprehensionFragment = new QuestionComprehensionFragment(entity.getQuestion5s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, chapterId, "");
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
                        answerFragment = new QuestionAnswerFragment(entity.getQuestion3s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, chapterId, answer);

                    } else {
                        answerJudge.put(fragmentList.size(), 0);
                        answerFragment = new QuestionAnswerFragment(entity.getQuestion3s().get(i), QuestionActivity.this, QuestionActivity.this, fragmentList.size(), questionNumber, chapterId, "");
                    }
                    fragmentList.add(answerFragment);
                }
                adapter.notifyDataSetChanged();
                if (questionNumber > 0) {
                    tv_question_number.setText("1/" + questionNumber);
                    if (progressNow > 0) {
                        vp_container.setCurrentItem(progressNow - 1);
                        tv_question_number.setText(progressNow + "/" + questionNumber);
                    }
                    cb_answer.setClickable(true);
                    cb_sheet.setClickable(true);
                    cb_collect.setClickable(true);
                    cb_note.setClickable(true);
                    cb_submit.setClickable(true);
                    //初始化收藏  在请求成功初始第一题收藏 和页面切换时修改收藏状态
                    cb_collect.setChecked("1".equals(collectJudge.get(0)));
                    questionLoading = true;
                    initTimer();
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<QuestionEntity> call, Throwable t) {
                Log.e("ts", "请求失败");
                loading.setVisibility(View.GONE);

            }
        });
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
                    currentAnswerJudge = answerJudge.get(vp_container.getCurrentItem());
                    Log.e(vp_container.getCurrentItem() + "", currentAnswerJudge + "");
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
                    if ("record".equals(from)) {
                        ToastUtils.showToast(this, "该模式不支持交卷");
                        cb_submit.setChecked(false);
                        cb_submit.setTextColor(Color.parseColor("#333333"));
                    } else {
                        showDialog();
                        cb_submit.setTextColor(Color.parseColor("#4397f7"));
                    }
                } else {
                    cb_submit.setTextColor(Color.parseColor("#333333"));
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
                    ToastUtils.showToast(QuestionActivity.this, "笔记不能为空");
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
                            ToastUtils.showToast(QuestionActivity.this, "修改成功");
                            cb_note.setChecked(false);
                        } else {
                            ToastUtils.showToast(QuestionActivity.this, "修改失败");
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
                            ToastUtils.showToast(QuestionActivity.this, "添加成功");
                            cb_note.setChecked(false);
                        } else {
                            ToastUtils.showToast(QuestionActivity.this, "添加失败");
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
        Log.e(vp_container.getCurrentItem() + "", currentAnswerJudge + "");
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
        answerJudge.put(vp_container.getCurrentItem(), currentAnswerJudge);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("record".equals(from)) {
                finish();
            } else {
                dialog = new CustomJJDialog(this);
                dialog.setTitle("提示");
                dialog.setMessage("是否结束本次测试");
                dialog.setYesOnclickListener("继续", new CustomJJDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setNoOnclickListener("退出", new CustomJJDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }
        }
        return true;
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
                        ToastUtils.showToast(QuestionActivity.this, "已收藏");
                        collectJudge.put(vp_container.getCurrentItem(), "1");
                    } else {
                        ToastUtils.showToast(QuestionActivity.this, message);
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
                        ToastUtils.showToast(QuestionActivity.this, "已取消");
                        collectJudge.put(vp_container.getCurrentItem(), "0");
                    } else {
                        ToastUtils.showToast(QuestionActivity.this, message);
                        cb_collect.setChecked(true);
                        collectJudge.put(vp_container.getCurrentItem(), "1");
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
        tv_question_number.setText((position + 1) + "/" + questionNumber);
        cb_collect.setChecked("1".equals(collectJudge.get(position)) ? true : false);
        if(position>0){
            if(fragmentList.get(position-1) instanceof QuestionCheckBoxQuestionFragment){
                ((QuestionCheckBoxQuestionFragment)fragmentList.get(position-1)).setError();
                Log.e("ts",position-1+"");
            }
        }
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
     * 交卷Dialog
     * correntNumber 正确数量
     * unanswerNumber  未答题数量
     * answerNumber 已答数量
     */
    private void showDialog() {
        //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功
        correntNumber = 0;
        uncorrentNumber = 0;
        unanswerNumber = 0;
        answerNumber = 0;
        for (int key : answerJudge.keySet()) {
            int value = answerJudge.get(key);
            if (value == 0) {
                unanswerNumber += 1;
            }
            if (value == 1) {
                correntNumber += 1;
                answerNumber += 1;
                Log.e("current", key + "");
            }
            if (value == 2) {
                Log.e("unCurrent", key + "");
                uncorrentNumber += 1;
                answerNumber += 1;
            }
        }
        dialog = new CustomJJDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("您还有<font color='#df3232'>" + unanswerNumber + "</font><font color='#333333'>道题未答，已用时</font><font color='#4397f8'>" + tv_question_time.getText().toString().trim() + "</font>");
        dialog.setYesOnclickListener("交卷", new CustomJJDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                dialog.dismiss();
                cb_submit.setChecked(false);
                Intent intent = new Intent(QuestionActivity.this, TestReportActivity.class);
                //除数不能为0 如果为0 则直接传0
                intent.putExtra("percent", answerNumber == 0 ? 0 : Math.round(correntNumber * 100f / answerNumber));
                intent.putExtra("time", tv_question_time.getText().toString().trim());
                intent.putExtra("wrong", answerNumber - correntNumber);
                intent.putExtra("right", correntNumber);
                intent.putExtra("count", answerNumber);
                intent.putExtra("chapterName", chapterName);
                intent.putExtra("chapterId", chapterId);
                intent.putExtra("subjectName", subjectName);
                intent.putExtra("subjectId", subjectId);
                intent.putExtra("chapterPaperType", chapterPaperType);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNoOnclickListener("继续答题", new CustomJJDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
                cb_submit.setChecked(false);
            }
        });
        dialog.show();
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
//            if(fragmentList.get(now) instanceof QuestionCheckBoxQuestionFragment){
//                ((QuestionCheckBoxQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).setError();
//            }
            vp_container.setCurrentItem(now - 1);

        } else if (type == 1) {
            now = vp_container.getCurrentItem();
//            if(fragmentList.get(now) instanceof QuestionCheckBoxQuestionFragment){
//                ((QuestionCheckBoxQuestionFragment) fragmentList.get(vp_container.getCurrentItem())).setError();
//            }
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

    private void initTimer() {
        if (timer == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    second += 1;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Date date = new Date(second * 1000);
                            Format format = new SimpleDateFormat("mm:ss");
                            tv_question_time.setText(format.format(date));
                        }
                    });
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 0, 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (questionLoading) {
            initTimer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timerTask.cancel();
            timer = null;
            timerTask = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            cb_sheet.setChecked(false);
            int jumpPosition = data.getIntExtra("jumpPosition", 0);
            vp_container.setCurrentItem(jumpPosition);
        }
    }
}
