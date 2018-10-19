package com.jsxl.selfexam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_register_phone, et_register_password, et_register_repassword, et_register_code;
    private TextView tv_register_send_code, tv_register;
    private RadioGroup rg_register;
    private RadioButton rb_register1, rb_register2, rb_register3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setStatusColor();
        initView();
        initData();
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_repassword = (EditText) findViewById(R.id.et_register_repassword);
        et_register_code = (EditText) findViewById(R.id.et_register_code);
        tv_register_send_code = (TextView) findViewById(R.id.tv_register_send_code);
        tv_register_send_code.setOnClickListener(this);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        rb_register1 = (RadioButton) findViewById(R.id.rb_register1);
        rb_register2 = (RadioButton) findViewById(R.id.rb_register2);
        rb_register3 = (RadioButton) findViewById(R.id.rb_register3);
    }

    @Override
    public void initData() {
        tv_title.setText("注册");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.tv_register_send_code:
                judgePhone();
                break;
            case R.id.tv_register:
                judgeEdit();
                break;
        }
    }

    private void judgePhone() {
        String phone;
        if (!NetWorkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
            return;
        } else {
            phone = et_register_phone.getText().toString().trim();
            if (phone.equals("") || phone == null) {
                Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isMobileNO(phone)) {
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
        Call<String> call2 = retrofitService2.sendCode("0", "1", "60", phone);
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
                        ToastUtils.showToast(RegisterActivity.this, message);
                    } else {
                        ToastUtils.showToast(RegisterActivity.this, message);
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

    private void judgeEdit() {
        String phone, password, repassword, code;
        if (!NetWorkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
            return;
        } else {
            phone = et_register_phone.getText().toString().trim();
            if (phone.equals("") || phone == null) {
                Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isMobileNO(phone)) {
                Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_LONG).show();
                return;
            }
            password = et_register_password.getText().toString().trim();
            if (password.equals("") || password == null) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            repassword = et_register_repassword.getText().toString().trim();
            if (repassword.equals("") || repassword == null) {
                Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(repassword)) {
                Toast.makeText(this, "输入密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() > 15 || password.length() < 6) {
                ToastUtils.showToast(this, "密码长度要在6-15位之间");
                return;
            }
            code = et_register_code.getText().toString().trim();
            if (code.length() != 6) {
                Toast.makeText(this, "请输入正确验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (code.equals("") || code == null) {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if ((!rb_register1.isChecked()) && (!rb_register2.isChecked()) && (!rb_register3.isChecked())) {
                Toast.makeText(this, "请选择专业", Toast.LENGTH_SHORT).show();
                return;
            }
            String subjectType;
            if (rb_register1.isChecked()) {
                subjectType = "0";
            } else if (rb_register2.isChecked()) {
                subjectType = "1";
            } else if (rb_register3.isChecked()) {
                subjectType = "2";
            }
            register(phone, password, code);
        }

    }

    private void register(String phone, String password, String code) {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.register(phone, password, "1", code);
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    if (success) {
                        ToastUtils.showToast(RegisterActivity.this, message);
//                        Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
//                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.showToast(RegisterActivity.this, message);
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
                                tv_register_send_code.setText("发送验证码");
                                tv_register_send_code.setClickable(true);
                            } else {
                                tv_register_send_code.setText(finalI + "秒后重发");
                                tv_register_send_code.setClickable(false);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
