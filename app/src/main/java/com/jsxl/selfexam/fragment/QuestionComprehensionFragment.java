package com.jsxl.selfexam.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.bean.QuestionEntity;
import com.jsxl.selfexam.database.DBHelper;
import com.jsxl.selfexam.interfaces.AnswerJudgeListener;
import com.jsxl.selfexam.interfaces.QuestionNextOrLastClickListener;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.ArticleContentDialog;
import com.jsxl.selfexam.widget.CustomDialog;

import static com.jsxl.selfexam.R.id.tv_solution;


public class QuestionComprehensionFragment extends Fragment implements View.OnClickListener {
    private QuestionEntity.Question5sBean question;
    private View view;
    private TextView tv_question_content;
    private EditText et_question_answer;
    private Button btn_next_question, btn_last_question;
    private QuestionNextOrLastClickListener listener;
    private AnswerJudgeListener answerJudgeListener;
    private int currentItem, total;
    private RatingBar rb_difficulty;
    private TextView tv_answer, tv_location_point, tv_knowledge_extend,tv_solution;
    private LinearLayout ll_analysis;
    private Button btn_question_more;
    private int chapterId;
    private DBHelper dbHelper;
    private String etText = "";
    private String endEtText = "";
    private String answer;
    @SuppressLint({"NewApi", "ValidFragment"})
    public QuestionComprehensionFragment(){

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public QuestionComprehensionFragment(QuestionEntity.Question5sBean question, QuestionNextOrLastClickListener listener, AnswerJudgeListener answerJudgeListener, int currentItem, int total,int chapterId,String answer) {
        // Required empty public constructor
        this.question = question;
        this.listener = listener;
        this.answerJudgeListener = answerJudgeListener;
        this.currentItem = currentItem;
        this.total = total;
        this.chapterId = chapterId;
        this.answer = answer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question_comprehension, container, false);
        dbHelper = new DBHelper(getActivity());
        initView();
        initData();
        return view;
    }

    private void initView() {
        tv_question_content = (TextView) view.findViewById(R.id.tv_question_content);
        et_question_answer = (EditText) view.findViewById(R.id.et_question_answer);
        btn_last_question = (Button) view.findViewById(R.id.btn_last_question);
        btn_last_question.setOnClickListener(this);
        btn_next_question = (Button) view.findViewById(R.id.btn_next_question);
        btn_next_question.setOnClickListener(this);
        rb_difficulty = (RatingBar) view.findViewById(R.id.rb_difficulty);
        tv_answer = (TextView) view.findViewById(R.id.tv_answer);
        tv_solution = (TextView) view.findViewById(R.id.tv_solution);
        tv_location_point = (TextView) view.findViewById(R.id.tv_location_point);
        tv_knowledge_extend = (TextView) view.findViewById(R.id.tv_knowledge_extend);
        ll_analysis = (LinearLayout) view.findViewById(R.id.ll_analysis);
        btn_question_more = (Button) view.findViewById(R.id.btn_question_more);
        btn_question_more.setOnClickListener(this);
    }

    public void showAnalysis(boolean isShow) {
        if (isShow) {
            ll_analysis.setVisibility(View.VISIBLE);
        } else {
            ll_analysis.setVisibility(View.GONE);
        }
    }
    public int getQuestionId(){
        return question.getId();
    }
    public String getCollect(){
        return question.getIsCollect();
    }
    private void initData() {
        //上一题 下一题变颜色
        if (currentItem == 0) {
            btn_last_question.setBackgroundResource(R.drawable.next_or_last_uncheck_bg);
        }
        if (currentItem == total - 1) {
            btn_next_question.setBackgroundResource(R.drawable.next_or_last_uncheck_bg);
        }
        tv_question_content.setText(Html.fromHtml(question.getQuestionContent()));
        initRatingBar();
        tv_answer.setText(Html.fromHtml("<b><tt>【参考答案】</tt></b>"
                + question.getQuestionAnswer()));
        tv_solution.setText(Html.fromHtml("<b><tt>【题目解析】</tt></b>"
                + question.getQuestionSolution()));
        tv_location_point.setText(Html.fromHtml("<b><tt>【考点定位】</tt></b>"
                + question.getLocationExaminationPoints()));
        tv_knowledge_extend.setText(Html.fromHtml("<b><tt>【知识扩展】</tt></b>"
                + question.getKnowledgeExtend()));
        etText = et_question_answer.getText().toString().trim();
        et_question_answer.setText(answer);
        et_question_answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_question_answer.getText().toString().trim().length()==0){
                    answerJudgeListener.answerJudge(0);
                }else if(et_question_answer.getText().toString().trim().length()>0){
                    answerJudgeListener.answerJudge(4);
                }
                endEtText = et_question_answer.getText().toString().trim();
                updateRecordToDB();
            }
        });
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
            case R.id.btn_question_more:
                showDialog();
                break;
        }
    }
    //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 4表示阅读填写成功
    private void  updateRecordToDB() {
        if(endEtText.length()==0&&etText.length()>0) {
            dbHelper.delQuestionRecord(question.getId());
        }
        if(endEtText.length()>0&&(!endEtText.equals(etText)))
            dbHelper.addQuestionRecord(chapterId,question.getId(),3,et_question_answer.getText().toString().trim(),4);
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
}
