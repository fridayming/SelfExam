package com.jsxl.selfexam.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.bean.QuestionEntity;
import com.jsxl.selfexam.database.DBHelper;
import com.jsxl.selfexam.interfaces.AnswerJudgeListener;
import com.jsxl.selfexam.interfaces.QuestionNextOrLastClickListener;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.jsxl.selfexam.R.id.add;
import static com.jsxl.selfexam.R.id.rb_answer0;
import static com.jsxl.selfexam.R.id.rb_answer1;
import static com.jsxl.selfexam.R.id.rb_answer2;
import static com.jsxl.selfexam.R.id.rb_answer3;
import static com.jsxl.selfexam.R.id.rb_answer4;
import static com.jsxl.selfexam.R.id.rg_question;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionCheckBoxQuestionFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private View view;
    private QuestionEntity.Question2sBean question;
    private TextView tv_question_content;
    private Button btn_next_question, btn_last_question;
    private CheckBox cb_answer0, cb_answer1, cb_answer2, cb_answer3, cb_answer4;
    private QuestionNextOrLastClickListener listener;
    private AnswerJudgeListener answerJudgeListener;
    private int currentItem, total;
    private RatingBar rb_difficulty;
    private TextView tv_answer, tv_location_point, tv_knowledge_extend, tv_solution;
    private LinearLayout ll_analysis;
    private Drawable drawRight, drawWrong, drawNomal;
    private boolean isTest;
    private int chapterId;
    private DBHelper dbHelper;
    private String answer;
    private SharedPreferences sp;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;

    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private String myAnswer;

    @SuppressLint({"NewApi", "ValidFragment"})
    public QuestionCheckBoxQuestionFragment() {
        // Required empty public constructor
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public QuestionCheckBoxQuestionFragment(QuestionEntity.Question2sBean question, QuestionNextOrLastClickListener listener, AnswerJudgeListener answerJudgeListener, int currentItem, int total, boolean isTest, int chapterId, String answer) {
        // Required empty public constructor
        this.question = question;
        this.listener = listener;
        this.answerJudgeListener = answerJudgeListener;
        this.currentItem = currentItem;
        this.total = total;
        this.isTest = isTest;
        this.chapterId = chapterId;
        this.answer = answer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question_check_box_question, container, false);
        dbHelper = new DBHelper(getActivity());
        sp = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        isViewCreated = true;
        initView();
        initData();
        lazyLoad();
        return view;
    }

    private void initData() {
        drawRight = getResources().getDrawable(R.drawable.icon_judge_state_right);
        drawWrong = getResources().getDrawable(R.drawable.icon_judge_state_wrong);
        drawNomal = getResources().getDrawable(R.drawable.icon_judge_uncheck);

        //上一题 下一题变颜色
        if (currentItem == 0) {
            btn_last_question.setBackgroundResource(R.drawable.next_or_last_uncheck_bg);
        }
        if (currentItem == total - 1) {
            btn_next_question.setBackgroundResource(R.drawable.next_or_last_uncheck_bg);
        }
        initRatingBar();
        tv_answer.setText(Html.fromHtml("<b><tt>【参考答案】</tt></b>"
                + question.getQuestionAnswer()));
        tv_solution.setText(Html.fromHtml("<b><tt>【题目解析】</tt></b>"
                + question.getQuestionSolution()));
        tv_location_point.setText(Html.fromHtml("<b><tt>【考点定位】</tt></b>"
                + question.getLocationExaminationPoints()));
        tv_knowledge_extend.setText(Html.fromHtml("<b><tt>【知识扩展】</tt></b>"
                + question.getKnowledgeExtend()));
//        question.getQuestionSolution()
        tv_question_content.setText(Html.fromHtml(question.getQuestionContent()));
        int answerNumber = question.getItemList().size();
        if (answerNumber <= 4) {
            cb_answer4.setVisibility(View.GONE);
        }
        if (answerNumber <= 3) {
            cb_answer3.setVisibility(View.GONE);
        }
        if (answerNumber <= 2) {
            cb_answer2.setVisibility(View.GONE);
        }
        if (answerNumber <= 1) {
            cb_answer1.setVisibility(View.GONE);
        }
        if (answerNumber <= 0) {
            cb_answer0.setVisibility(View.GONE);
        }
        for (int i = 0; i < question.getItemList().size(); i++) {
            if (i == 0) {
                cb_answer0.setText(Html.fromHtml("A." + question.getItemList().get(i).getQuestionItemContent()));
            }
            if (i == 1) {
                cb_answer1.setText(Html.fromHtml("B." + question.getItemList().get(i).getQuestionItemContent()));
            }
            if (i == 2) {
                cb_answer2.setText(Html.fromHtml("C." + question.getItemList().get(i).getQuestionItemContent()));
            }
            if (i == 3) {
                cb_answer3.setText(Html.fromHtml("D." + question.getItemList().get(i).getQuestionItemContent()));
            }
            if (i == 4) {
                cb_answer4.setText(Html.fromHtml("E." + question.getItemList().get(i).getQuestionItemContent()));
            }

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            setCheck();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    private void initView() {
        tv_question_content = (TextView) view.findViewById(R.id.tv_question_content);
        btn_last_question = (Button) view.findViewById(R.id.btn_last_question);
        btn_last_question.setOnClickListener(this);
        btn_next_question = (Button) view.findViewById(R.id.btn_next_question);
        btn_next_question.setOnClickListener(this);
        cb_answer0 = (CheckBox) view.findViewById(R.id.cb_answer0);
        cb_answer0.setOnCheckedChangeListener(this);
        cb_answer1 = (CheckBox) view.findViewById(R.id.cb_answer1);
        cb_answer1.setOnCheckedChangeListener(this);
        cb_answer2 = (CheckBox) view.findViewById(R.id.cb_answer2);
        cb_answer2.setOnCheckedChangeListener(this);
        cb_answer3 = (CheckBox) view.findViewById(R.id.cb_answer3);
        cb_answer3.setOnCheckedChangeListener(this);
        cb_answer4 = (CheckBox) view.findViewById(R.id.cb_answer4);
        cb_answer4.setOnCheckedChangeListener(this);
        rb_difficulty = (RatingBar) view.findViewById(R.id.rb_difficulty);
        tv_answer = (TextView) view.findViewById(R.id.tv_answer);
        tv_solution = (TextView) view.findViewById(R.id.tv_solution);
        tv_location_point = (TextView) view.findViewById(R.id.tv_location_point);
        tv_knowledge_extend = (TextView) view.findViewById(R.id.tv_knowledge_extend);
        ll_analysis = (LinearLayout) view.findViewById(R.id.ll_analysis);

    }

    private void setCheck() {
        if (answer.contains("A")) {
            cb_answer0.setChecked(true);
        }
        if (answer.contains("B")) {
            cb_answer1.setChecked(true);
        }
        if (answer.contains("C")) {
            cb_answer2.setChecked(true);
        }
        if (answer.contains("D")) {
            cb_answer3.setChecked(true);
        }
        if (answer.contains("E")) {
            cb_answer4.setChecked(true);
        }
    }

    public void showAnalysis(boolean isShow) {
        if (isShow) {
            ll_analysis.setVisibility(View.VISIBLE);
        } else {
            ll_analysis.setVisibility(View.GONE);
        }
    }

    private void initRatingBar() {
        // 获取图片的高度
        int starsImgHeight = 0;
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    R.drawable.icon_stars);
            starsImgHeight = bmp.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 将获取的图片高度设置给RatingBar
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rb_difficulty
                .getLayoutParams();
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.height = starsImgHeight;
        rb_difficulty.setLayoutParams(lp);
        rb_difficulty.setRating(Float.parseFloat(question.getDegreeDifficulty() + ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last_question:
                listener.nextOrLast(0);
                break;
            case R.id.btn_next_question:
                listener.nextOrLast(1);
                break;
        }
    }

    public int getQuestionId() {
        return question.getId();
    }

    public void setError() {
        Log.e("ttt",judgeAnswer()+"");
        if(judgeAnswer()){
            delError();
        }else{
            addError();
        }
    }

    private boolean judgeAnswer() {
        myAnswer = "";
        if (cb_answer0.isChecked()) {
            myAnswer += "A";
        }
        if (cb_answer1.isChecked()) {
            myAnswer += "B";
        }
        if (cb_answer2.isChecked()) {
            myAnswer += "C";
        }
        if (cb_answer3.isChecked()) {
            myAnswer += "D";
        }
        if (cb_answer4.isChecked()) {
            myAnswer += "E";
        }
        //如果答案未选择 也判断做题正确
        if (myAnswer.length()==0||question.getQuestionAnswer().equals(myAnswer)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_answer0:
                setAnswer();
//                if (question.getQuestionAnswer().equals("A")) {
//                    answerJudgeListener.answerJudge(1);
//                    delError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawRight, cb_answer0);
//                    }
//                    updateRecordFoDB("A", 1);
//                } else {
//                    answerJudgeListener.answerJudge(2);
//                    addError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawWrong, cb_answer0);
//                    }
//                    updateRecordFoDB("A", 2);
//                }
                break;
            case R.id.cb_answer1:
                setAnswer();
//                if (question.getQuestionAnswer().equals("B")) {
//                    answerJudgeListener.answerJudge(1);
//                    delError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawRight, cb_answer1);
//                    }
//                    updateRecordFoDB("B", 1);
//                } else {
//                    answerJudgeListener.answerJudge(2);
//                    addError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawWrong, cb_answer1);
//                    }
//                    updateRecordFoDB("B", 2);
//                }
                break;
            case R.id.cb_answer2:
                setAnswer();
//                if (question.getQuestionAnswer().equals("C")) {
//                    answerJudgeListener.answerJudge(1);
//                    delError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawRight, cb_answer2);
//                    }
//                    updateRecordFoDB("C", 1);
//                } else {
//                    answerJudgeListener.answerJudge(2);
//                    addError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawWrong, cb_answer2);
//                    }
//                    updateRecordFoDB("C", 2);
//                }
                break;
            case R.id.cb_answer3:
                setAnswer();
//                if (question.getQuestionAnswer().equals("D")) {
//                    answerJudgeListener.answerJudge(1);
//                    delError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawRight, cb_answer3);
//                    }
//                    updateRecordFoDB("D", 1);
//                } else {
//                    answerJudgeListener.answerJudge(2);
//                    addError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawWrong, cb_answer3);
//                    }
//                    updateRecordFoDB("D", 2);
//                }
                break;
            case R.id.cb_answer4:
                setAnswer();
//                if (question.getQuestionAnswer().equals("E")) {
//                    answerJudgeListener.answerJudge(1);
//                    delError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawRight, cb_answer4);
//                    }
//                    updateRecordFoDB("E", 1);
//                } else {
//                    answerJudgeListener.answerJudge(2);
//                    addError();
//                    if (isTest) {
//                        setRadioButtonDraw(drawWrong, cb_answer4);
//                    }
//                    updateRecordFoDB("E", 2);
//                }
                break;
        }
    }

    private void setAnswer() {
        myAnswer = "";
        if (cb_answer0.isChecked()) {
            myAnswer += "A";
        }
        if (cb_answer1.isChecked()) {
            myAnswer += "B";
        }
        if (cb_answer2.isChecked()) {
            myAnswer += "C";
        }
        if (cb_answer3.isChecked()) {
            myAnswer += "D";
        }
        if (cb_answer4.isChecked()) {
            myAnswer += "E";
        }
        if (myAnswer.length() == 0) {
            answerJudgeListener.answerJudge(0);
            updateRecordFoDB(myAnswer, 0);
        } else {
            if (question.getQuestionAnswer().equals(myAnswer)) {
                answerJudgeListener.answerJudge(1);
//            delError();
                //多选不管测试模式 练习模式都不显示对错
//            if (isTest) {
//                setRadioButtonDraw(drawRight, cb_answer4);
//            }
                updateRecordFoDB(myAnswer, 1);
            } else {
                answerJudgeListener.answerJudge(2);
//            addError();
//            if (isTest) {
//                setRadioButtonDraw(drawRight, cb_answer4);
//            }
                updateRecordFoDB(myAnswer, 2);
            }
        }
    }

//    private void setRadioButtonDraw(Drawable draw, CheckBox cb) {
//        cb_answer0.setChecked(false);
//        cb_answer0.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
//        cb_answer1.setChecked(false);
//        cb_answer1.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
//        cb_answer2.setChecked(false);
//        cb_answer2.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
//        cb_answer3.setChecked(false);
//        cb_answer3.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
//        cb_answer4.setChecked(false);
//        cb_answer4.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
//        cb.setChecked(true);
//        cb.setCompoundDrawablesRelativeWithIntrinsicBounds(draw, null, null, null);
//    }

    /**
     * 添加错题
     *
     * @param
     */
    private void addError() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.addError(sp.getString("userId", ""), chapterId + "", question.getId() + "");
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /**
     * 取消错题
     *
     * @param
     */
    private void delError() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.delError(sp.getString("userId", ""), chapterId + "", question.getId() + "");
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功
    private void updateRecordFoDB(String answer, int judge) {
        if (judge == 0) {
            dbHelper.delQuestionRecord(question.getId());
        } else {
            dbHelper.addQuestionRecord(chapterId, question.getId(), 3, answer, judge);
        }
    }


}
