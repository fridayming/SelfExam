package com.jsxl.selfexam.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.jsxl.selfexam.widget.ArticleContentDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.jsxl.selfexam.R.id.add;
import static com.jsxl.selfexam.R.id.rb_answer4;
import static com.jsxl.selfexam.R.id.tv_solution;


public class QuestionComprehensionSelectFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View view;
    private QuestionEntity.Question6sBean question;
    private TextView tv_question_content;
    private EditText et_question_answer;
    private Button btn_next_question, btn_last_question;
    private RadioGroup rg_question;
    private RadioButton rb_answer0, rb_answer1, rb_answer2, rb_answer3;
    private QuestionNextOrLastClickListener listener;
    private AnswerJudgeListener answerJudgeListener;
    private int currentItem, total;
    private RatingBar rb_difficulty;
    private TextView tv_answer, tv_location_point, tv_knowledge_extend,tv_solution;
    private LinearLayout ll_analysis;
    private Button btn_question_more;
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
    @SuppressLint({"NewApi", "ValidFragment"})
    public QuestionComprehensionSelectFragment(){

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public QuestionComprehensionSelectFragment(QuestionEntity.Question6sBean question, QuestionNextOrLastClickListener listener, AnswerJudgeListener answerJudgeListener, int currentItem, int total, boolean isTest, int chapterId, String answer) {
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
        view = inflater.inflate(R.layout.fragment_question_comprehension_select, container, false);
        dbHelper = new DBHelper(getActivity());
        sp = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        initView();
        initData();
        lazyLoad();
        return view;
    }

    private void initView() {
        tv_question_content = (TextView) view.findViewById(R.id.tv_question_content);
        btn_last_question = (Button) view.findViewById(R.id.btn_last_question);
        btn_last_question.setOnClickListener(this);
        btn_next_question = (Button) view.findViewById(R.id.btn_next_question);
        btn_next_question.setOnClickListener(this);
        rg_question = (RadioGroup) view.findViewById(R.id.rg_question);
        rg_question.setOnCheckedChangeListener(this);
        rb_answer0 = (RadioButton) view.findViewById(R.id.rb_answer0);
        rb_answer1 = (RadioButton) view.findViewById(R.id.rb_answer1);
        rb_answer2 = (RadioButton) view.findViewById(R.id.rb_answer2);
        rb_answer3 = (RadioButton) view.findViewById(R.id.rb_answer3);
        rb_difficulty = (RatingBar) view.findViewById(R.id.rb_difficulty);
        tv_answer = (TextView) view.findViewById(R.id.tv_answer);
        tv_solution = (TextView) view.findViewById(R.id.tv_solution);
        tv_location_point = (TextView) view.findViewById(R.id.tv_location_point);
        tv_knowledge_extend = (TextView) view.findViewById(R.id.tv_knowledge_extend);
        ll_analysis = (LinearLayout) view.findViewById(R.id.ll_analysis);
        btn_question_more = (Button) view.findViewById(R.id.btn_question_more);
        btn_question_more.setOnClickListener(this);
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
        tv_question_content.setText(Html.fromHtml(question.getQuestionContent()));
        rb_answer0.setText(Html.fromHtml("A." + question.getItemList().get(0).getQuestionItemContent()));
        rb_answer1.setText(Html.fromHtml("B." + question.getItemList().get(1).getQuestionItemContent()));
        rb_answer2.setText(Html.fromHtml("C." + question.getItemList().get(2).getQuestionItemContent()));

        rb_answer3.setVisibility(View.GONE);
        if (question.getItemList().size() >= 4) {
            rb_answer3.setVisibility(View.VISIBLE);
            rb_answer3.setText(Html.fromHtml("D." + question.getItemList().get(3).getQuestionItemContent()));
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

    private void setCheck() {
        if ("A".equals(answer)) {
            rb_answer0.setChecked(true);
        } else if ("B".equals(answer)) {
            rb_answer1.setChecked(true);
        } else if ("C".equals(answer)) {
            rb_answer2.setChecked(true);
        } else if ("D".equals(answer)) {
            rb_answer3.setChecked(true);
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

    public int getQuestionId() {
        return question.getId();
    }

    public String getCollect() {
        return question.getIsCollect();
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
            case R.id.btn_question_more:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        final ArticleContentDialog dialog = new ArticleContentDialog(getActivity());
        dialog.setMessage(question.getArticleMsg().getArticleContent());
        dialog.setNoOnclickListener(new ArticleContentDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setRadioButtonDraw(Drawable draw, RadioButton rb) {
        rb_answer0.setChecked(false);
        rb_answer0.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
        rb_answer1.setChecked(false);
        rb_answer1.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
        rb_answer2.setChecked(false);
        rb_answer2.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
        rb_answer3.setChecked(false);
        rb_answer3.setCompoundDrawablesRelativeWithIntrinsicBounds(drawNomal, null, null, null);
        rb.setChecked(true);
        rb.setCompoundDrawablesRelativeWithIntrinsicBounds(draw, null, null, null);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_answer0:
                if (question.getQuestionAnswer().equals("A")) {
                    answerJudgeListener.answerJudge(1);
                    delError();
                    if (isTest) {
                        setRadioButtonDraw(drawRight, rb_answer0);
                    }
                    updateRecordFoDB("A", 1);
                } else {
                    answerJudgeListener.answerJudge(2);
                    addError();
                    if (isTest) {
                        setRadioButtonDraw(drawWrong, rb_answer0);
                    }
                    updateRecordFoDB("A", 2);
                }
                break;
            case R.id.rb_answer1:
                if (question.getQuestionAnswer().equals("B")) {
                    answerJudgeListener.answerJudge(1);
                    delError();
                    if (isTest) {
                        setRadioButtonDraw(drawRight, rb_answer1);
                    }
                    updateRecordFoDB("B", 1);
                } else {
                    answerJudgeListener.answerJudge(2);
                    addError();
                    if (isTest) {
                        setRadioButtonDraw(drawWrong, rb_answer1);
                    }
                    updateRecordFoDB("B", 2);
                }
                break;
            case R.id.rb_answer2:
                if (question.getQuestionAnswer().equals("C")) {
                    answerJudgeListener.answerJudge(1);
                    delError();
                    if (isTest) {
                        setRadioButtonDraw(drawRight, rb_answer2);
                    }
                    updateRecordFoDB("C", 1);
                } else {
                    answerJudgeListener.answerJudge(2);
                    addError();
                    if (isTest) {
                        setRadioButtonDraw(drawWrong, rb_answer2);
                    }
                    updateRecordFoDB("C", 2);
                }
                break;
            case R.id.rb_answer3:
                if (question.getQuestionAnswer().equals("D")) {
                    answerJudgeListener.answerJudge(1);
                    delError();
                    if (isTest) {
                        setRadioButtonDraw(drawRight, rb_answer3);
                    }
                    updateRecordFoDB("D", 1);
                } else {
                    answerJudgeListener.answerJudge(2);
                    addError();
                    if (isTest) {
                        setRadioButtonDraw(drawWrong, rb_answer3);
                    }
                    updateRecordFoDB("D", 2);
                }
                break;
        }
    }

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
    private void updateRecordFoDB(String answer, int jusge) {
        dbHelper.addQuestionRecord(chapterId, question.getId(), 3, answer, jusge);
    }
}
