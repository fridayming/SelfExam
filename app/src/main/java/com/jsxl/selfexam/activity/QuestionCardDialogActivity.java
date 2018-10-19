package com.jsxl.selfexam.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.adapter.QuestionCardAdapter;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.widget.DrawableTextView;
import com.jsxl.selfexam.widget.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QuestionCardDialogActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private DrawableTextView tv_question_card_right, tv_question_card_wrong, tv_question_card_total;
    private ImageView iv_question_card_back;
    //键用来记录1单选 2多选 3完型 4阅读  5简答题 值为题目数量用来传递给答题卡
    private HashMap<Integer, Integer> questionTypeNumber;
    //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功
    private HashMap<Integer, Integer> answerJudge;
    //从左到右依次为单选 多选 完型 阅读 简答
    private TextView tv_question_card_radio, tv_question_card_check, tv_question_card_clozetest, tv_question_card_comprehension_select, tv_question_card_comprehension, tv_question_card_answer;
    private MyGridView gv_question_card_radio, gv_question_card_check, gv_question_card_clozetest, gv_question_card_comprehension_select, gv_question_card_comprehension, gv_question_card_answer;
    private List<Integer> list1, list2, list3, list4, list5, list6;
    private int now = 0;
    private QuestionCardAdapter adapter1, adapter2, adapter3, adapter4, adapter5, adapter6;
    private Integer currentItem, right, wrong, answer, allQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_card_dialog);
        setStatusColorForBanner();
        questionTypeNumber = (HashMap<Integer, Integer>) getIntent().getSerializableExtra("questionTypeNumber");
        answerJudge = (HashMap<Integer, Integer>) getIntent().getSerializableExtra("answerJudge");
        currentItem = getIntent().getIntExtra("currentItem", 0);
        right = getIntent().getIntExtra("right", 0);
        wrong = getIntent().getIntExtra("wrong", 0);
        answer = getIntent().getIntExtra("answer", 0);
        allQuestion = getIntent().getIntExtra("allQuestion", 0);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        tv_question_card_right = (DrawableTextView) findViewById(R.id.tv_question_card_right);
        tv_question_card_wrong = (DrawableTextView) findViewById(R.id.tv_question_card_wrong);
        tv_question_card_total = (DrawableTextView) findViewById(R.id.tv_question_card_total);
        iv_question_card_back = (ImageView) findViewById(R.id.iv_question_card_back);
        iv_question_card_back.setOnClickListener(this);
        tv_question_card_radio = (TextView) findViewById(R.id.tv_question_card_radio);
        tv_question_card_check = (TextView) findViewById(R.id.tv_question_card_check);
        tv_question_card_clozetest = (TextView) findViewById(R.id.tv_question_card_clozetest);
        tv_question_card_comprehension_select = (TextView) findViewById(R.id.tv_question_card_comprehension_select);
        tv_question_card_comprehension = (TextView) findViewById(R.id.tv_question_card_comprehension);
        tv_question_card_answer = (TextView) findViewById(R.id.tv_question_card_answer);
        gv_question_card_radio = (MyGridView) findViewById(R.id.gv_question_card_radio);
        gv_question_card_radio.setOnItemClickListener(this);
        gv_question_card_check = (MyGridView) findViewById(R.id.gv_question_card_check);
        gv_question_card_check.setOnItemClickListener(this);
        gv_question_card_clozetest = (MyGridView) findViewById(R.id.gv_question_card_clozetest);
        gv_question_card_clozetest.setOnItemClickListener(this);
        gv_question_card_comprehension_select = (MyGridView) findViewById(R.id.gv_question_card_comprehension_select);
        gv_question_card_comprehension_select.setOnItemClickListener(this);
        gv_question_card_comprehension = (MyGridView) findViewById(R.id.gv_question_card_comprehension);
        gv_question_card_comprehension.setOnItemClickListener(this);
        gv_question_card_answer = (MyGridView) findViewById(R.id.gv_question_card_answer);
        gv_question_card_answer.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        tv_question_card_right.setText(right + "");
        tv_question_card_wrong.setText(wrong + "");
        tv_question_card_total.setText(answer + "/" + allQuestion + "");
        //键用来记录1单选 2多选 3简答题 4完型 5阅读型问答 6阅读型单选
        if (questionTypeNumber.containsKey(2) && questionTypeNumber.get(2) > 0) {
            tv_question_card_check.setVisibility(View.VISIBLE);
            list2 = new ArrayList<>();
            for (int i = 0; i < questionTypeNumber.get(2); i++) {
                now += 1;
                list2.add(now);
                adapter2 = new QuestionCardAdapter(this, list2, answerJudge);
                gv_question_card_check.setAdapter(adapter2);
            }
        }
        if (questionTypeNumber.containsKey(1) && questionTypeNumber.get(1) > 0) {
            tv_question_card_radio.setVisibility(View.VISIBLE);
            list1 = new ArrayList<>();
            for (int i = 0; i < questionTypeNumber.get(1); i++) {
                now += 1;
                list1.add(now);
                adapter1 = new QuestionCardAdapter(this, list1, answerJudge);
                gv_question_card_radio.setAdapter(adapter1);
            }

        }

        if (questionTypeNumber.containsKey(4) && questionTypeNumber.get(4) > 0) {
            tv_question_card_clozetest.setVisibility(View.VISIBLE);
            list4 = new ArrayList<>();
            for (int i = 0; i < questionTypeNumber.get(4); i++) {
                now += 1;
                list4.add(now);
                adapter4 = new QuestionCardAdapter(this, list4, answerJudge);
                gv_question_card_clozetest.setAdapter(adapter4);
            }
        }
        if (questionTypeNumber.containsKey(6) && questionTypeNumber.get(6) > 0) {
            tv_question_card_comprehension_select.setVisibility(View.VISIBLE);
            list6 = new ArrayList<>();
            for (int i = 0; i < questionTypeNumber.get(6); i++) {
                now += 1;
                list6.add(now);
                adapter6 = new QuestionCardAdapter(this, list6, answerJudge);
                gv_question_card_comprehension_select.setAdapter(adapter6);
            }
        }
        if (questionTypeNumber.containsKey(5) && questionTypeNumber.get(5) > 0) {
            tv_question_card_comprehension.setVisibility(View.VISIBLE);
            list5 = new ArrayList<>();
            for (int i = 0; i < questionTypeNumber.get(5); i++) {
                now += 1;
                list5.add(now);
                adapter5 = new QuestionCardAdapter(this, list5, answerJudge);
                gv_question_card_comprehension.setAdapter(adapter5);
            }
        }
        if (questionTypeNumber.containsKey(3) && questionTypeNumber.get(3) > 0) {
            tv_question_card_answer.setVisibility(View.VISIBLE);
            list3 = new ArrayList<>();
            for (int i = 0; i < questionTypeNumber.get(3); i++) {
                now += 1;
                list3.add(now);
                adapter3 = new QuestionCardAdapter(this, list3, answerJudge);
                gv_question_card_answer.setAdapter(adapter3);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_question_card_back:
                Intent intent = new Intent();
                intent.putExtra("jumpPosition", currentItem);
                setResult(0, intent);
                finish();
                overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (parent.getId()) {
            case R.id.gv_question_card_radio:
                intent.putExtra("jumpPosition", list1.get(position) - 1);
                this.setResult(0, intent);
                this.finish();
                overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
                break;
            case R.id.gv_question_card_check:
                intent.putExtra("jumpPosition", list2.get(position) - 1);
                this.setResult(0, intent);
                this.finish();
                overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
                break;
            case R.id.gv_question_card_clozetest:
                intent.putExtra("jumpPosition", list4.get(position) - 1);
                this.setResult(0, intent);
                this.finish();
                overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
                break;
            case R.id.gv_question_card_comprehension_select:
                intent.putExtra("jumpPosition", list6.get(position) - 1);
                this.setResult(0, intent);
                this.finish();
                overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
                break;
            case R.id.gv_question_card_comprehension:
                intent.putExtra("jumpPosition", list5.get(position) - 1);
                this.setResult(0, intent);
                this.finish();
                overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
                break;
            case R.id.gv_question_card_answer:
                intent.putExtra("jumpPosition", list3.get(position) - 1);
                this.setResult(0, intent);
                this.finish();
                overridePendingTransition(R.anim.dialog_activity_show, R.anim.dialog_activity_hide);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtra("jumpPosition", currentItem);
        this.setResult(0, intent);
        this.finish();
        return false;
    }
}
