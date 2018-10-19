package com.jsxl.selfexam.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;
import com.jsxl.selfexam.interfaces.RetrofitService;
import com.jsxl.selfexam.utils.NetWorkUtils;
import com.jsxl.selfexam.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrievePasswordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_retrieve_phone, et_retrieve_checkcode;
    private TextView tv_retrieve_send_code, tv_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);
        setStatusColor();
        initView();
        initData();

    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_retrieve_phone = (EditText) findViewById(R.id.et_retrieve_phone);
        et_retrieve_checkcode = (EditText) findViewById(R.id.et_retrieve_checkcode);
        tv_retrieve_send_code = (TextView) findViewById(R.id.tv_retrieve_send_code);
        tv_retrieve_send_code.setOnClickListener(this);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);

    }

    @Override
    public void initData() {
        tv_title.setText("找回密码");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.tv_retrieve_send_code:
                judgePhone();
                break;
            case R.id.tv_submit:
                checkCode();
                break;
        }
    }

    private void checkCode() {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.checkCode("0", "0", "60", et_retrieve_phone.getText().toString().trim(),et_retrieve_checkcode.getText().toString().trim());
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast(RetrievePasswordActivity.this, message);
                        Intent intent = new Intent(RetrievePasswordActivity.this,ModifyPasswordActivity.class);
                        intent.putExtra("mobile", et_retrieve_phone.getText().toString().trim());
                        intent.putExtra("from","retrievePassword");
                        startActivity(intent);
                        RetrievePasswordActivity.this.finish();
                    } else {
                        ToastUtils.showToast(RetrievePasswordActivity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void judgePhone() {
        String phone;
        if (!NetWorkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
            return;
        } else {
            phone = et_retrieve_phone.getText().toString().trim();
            if (phone.equals("") || phone == null) {
                Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!RegisterActivity.isMobileNO(phone)) {
                Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_LONG).show();
                return;
            }
            sendCode(phone);
        }
    }
    private void sendCode(String phone) {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.sendCode("0", "0", "60", phone);
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        countDown();
                        ToastUtils.showToast(RetrievePasswordActivity.this, message);
                    } else {
                        ToastUtils.showToast(RetrievePasswordActivity.this, message);
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
    Handler handler = new Handler() {
    };
    /**
     * 倒计时修改文字
     */
    public void countDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 59; i >= 0; i--) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = i;
                    final int finalI = i;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (finalI == 0) {
                                tv_retrieve_send_code.setText("发送验证码");
                                tv_retrieve_send_code.setClickable(true);
                            } else {
                                tv_retrieve_send_code.setText(finalI + "秒后重发");
                                tv_retrieve_send_code.setClickable(false);
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
