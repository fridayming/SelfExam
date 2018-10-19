package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ExamTypeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_submit;
    private RadioButton rb_register1, rb_register2, rb_register3;
    private SharedPreferences sp;
    private String checkId = "1";
    private CustomDialog dialog;
    private String examId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_type);
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        rb_register1 = (RadioButton) findViewById(R.id.rb_register1);
        rb_register1.setOnClickListener(this);
        rb_register2 = (RadioButton) findViewById(R.id.rb_register2);
        rb_register2.setOnClickListener(this);
        rb_register3 = (RadioButton) findViewById(R.id.rb_register3);
        rb_register3.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("报考类型");
        examId = sp.getString("examId","0");
        if(examId.equals("1")){
            rb_register1.setChecked(true);
        }else if(examId.equals("2")){
            rb_register2.setChecked(true);
        }else if(examId.equals("3")){
            rb_register3.setChecked(true);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                if (rb_register1.isChecked()) {
                    checkId = "1";
                } else if (rb_register2.isChecked()) {
                    checkId = "2";
                } else if (rb_register3.isChecked()) {
                    checkId = "3";
                }
                showDialog();

                break;
            case R.id.rb_register1:
                rb_register2.setChecked(false);
                rb_register3.setChecked(false);
                break;
            case R.id.rb_register2:
                rb_register1.setChecked(false);
                rb_register3.setChecked(false);
                break;
            case R.id.rb_register3:
                rb_register1.setChecked(false);
                rb_register2.setChecked(false);
                break;
        }
    }

    private void showDialog() {
        dialog = new CustomDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("确认要切换报考类型?");
        dialog.setYesOnclickListener("确认", new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                updateExamType();
                dialog.dismiss();
            }
        });
        dialog.setNoOnclickListener("取消", new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateExamType() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl1)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.updateExamType(sp.getString("userId", ""), checkId);
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    SharedPreferences.Editor et = sp.edit();
                    if (success) {

                        ToastUtils.showToast(ExamTypeActivity.this, message);
                        if (checkId.equals("1")) {
                            et.putString("examName", "专升本");
                            et.putString("examId", "1");
                        }
                        if (checkId.equals("2")) {
                            et.putString("examName", "高起本");
                            et.putString("examId", "2");
                        }
                        if (checkId.equals("3")) {
                            et.putString("examName", "高起专");
                            et.putString("examId", "3");
                        }
                        et.commit();
                        finish();
                    } else {
                        ToastUtils.showToast(ExamTypeActivity.this, message);
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
