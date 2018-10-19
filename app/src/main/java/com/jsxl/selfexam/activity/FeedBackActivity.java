package com.jsxl.selfexam.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.R.attr.password;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_feedback;
    private TextView tv_text_number;
    private TextView tv_submit;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        sp = getSharedPreferences("USERINFO",MODE_PRIVATE);
        setStatusColor();
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        tv_text_number = (TextView) findViewById(R.id.tv_text_number);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("意见反馈");
        et_feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_text_number.setText(et_feedback.getText().toString().trim().length()+"/180");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                if(et_feedback.getText().toString().length()>0){
                    getBack();
                }else{
                    ToastUtils.showToast(this,"请输入提交内容");
                }
                break;
        }
    }
    public void getBack() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.feedback(sp.getString("userId",""), et_feedback.getText().toString());
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast(FeedBackActivity.this,message);
                        finish();
                    } else {
                        ToastUtils.showToast(FeedBackActivity.this,"提交失败");
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
