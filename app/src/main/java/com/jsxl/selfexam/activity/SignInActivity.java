package com.jsxl.selfexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.jsxl.selfexam.widget.CheckView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignInActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_signin_register;
    private EditText et_signin_phone, et_signin_password, et_signin_code;
    private TextView tv_signin;
    private TextView tv_retrieve_password;
    private CheckView iv_signin_code;
    private SharedPreferences sp;
    //产生的验证码
    private String rightCode = "";  //获取每次更新的验证码，可用于判断用户输入是否正确
    private boolean phonePass = false, passwordPass = false, codePass = false;
    private String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setStatusColor();
        sp = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        from = getIntent().getStringExtra("from");
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_signin_register = (TextView) findViewById(R.id.tv_signin_register);
        tv_signin_register.setOnClickListener(this);
        et_signin_phone = (EditText) findViewById(R.id.et_signin_phone);
        et_signin_phone.addTextChangedListener(this);
        et_signin_password = (EditText) findViewById(R.id.et_signin_password);
        et_signin_password.addTextChangedListener(this);
        et_signin_code = (EditText) findViewById(R.id.et_signin_code);
        et_signin_code.addTextChangedListener(this);
        tv_retrieve_password = (TextView) findViewById(R.id.tv_retrieve_password);
        tv_retrieve_password.setOnClickListener(this);
        tv_signin = (TextView) findViewById(R.id.tv_signin);
        tv_signin.setOnClickListener(this);
        iv_signin_code = (CheckView) findViewById(R.id.iv_signin_code);
        iv_signin_code.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        //初始化验证码
        tv_title.setText("用户登录");
        rightCode = iv_signin_code.getValidataAndSetImage();
        et_signin_phone.setText(sp.getString("mobile",""));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_signin_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_signin_code:
                upDataCode();
                break;
            case R.id.tv_signin:
                judgeEdit();
                break;
            case R.id.tv_retrieve_password:
                intent = new Intent(this, RetrievePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void judgeEdit() {
        String phone, password, code;
        if (!NetWorkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
            return;
        } else {
            phone = et_signin_phone.getText().toString().trim();
            if (phone.equals("") || phone == null) {
                Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isMobileNO(phone)) {
                Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_LONG).show();
                return;
            }
            password = et_signin_password.getText().toString().trim();
            if (password.equals("") || password == null) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() > 15 || password.length() < 6) {
                ToastUtils.showToast(this, "密码长度要在6-15位之间");
                return;
            }
            code = et_signin_code.getText().toString().trim();
            if (code.equals("") || code == null) {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!code.equals(rightCode)) {
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                upDataCode();
                return;
            }
            signIn(phone, password);
        }

    }

    @Override
    public void onBackPressed() {
            Intent intent= new Intent();
            intent.putExtra("success",false);
            setResult(0,intent);
            finish();
        super.onBackPressed();
    }

    //登录
    private void signIn(String phone, String password) {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
                .build();
        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
        Call<String> call2 = retrofitService2.login(phone, password);
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //测试数据返回
                try {
//                    {"message":"登录成功","ficPath":"http://static.jsxlmed.com/chengkao/image","entity":
// {"id":24,"stuName":"17611143690","gender":0,"mobile":"17611143690","password":"e10adc3949ba59abbe56e057f20f883e",
// "age":null,"integral":0,"examinationTypeId":1,"createUser":0,"createTime":1530154838000,"status":0,"fileId":11588,
// "examinationTypeName":"专升本","path":"/avatar/temp/2018628/1530155561032396.jpg"},"success":true}
                    Log.e("ts", response.body());
                    JSONObject js = new JSONObject(response.body());
                    boolean success = js.getBoolean("success");
                    String message = js.getString("message");
                    SharedPreferences.Editor et = sp.edit();
                    if (success) {

                        ToastUtils.showToast(SignInActivity.this, message);
                        JSONObject jso = js.getJSONObject("entity");
                        et.putBoolean("isLogin", true);
                        et.putString("userId", jso.getString("id"));
                        et.putString("userName", jso.getString("stuName"));
                        et.putString("headUrl", js.getString("ficPath")+jso.getString("path") );
                        //0男  1女
                        et.putInt("gender", jso.getInt("gender"));
                        et.putString("mobile", jso.getString("mobile"));
                        et.putString("age", jso.getString("age"));
                        et.putString("examId",jso.getString("examinationTypeId"));
                        et.putString("examName", jso.getString("examinationTypeName"));
                        et.commit();
                        Intent intent= new Intent();
                        intent.putExtra("success",true);
                        setResult(0,intent);
                        if("welcome".equals(from)){
                            Intent intent1 = new Intent(SignInActivity.this, HomePagerActivity.class);
                            startActivity(intent1);
                            finish();
                        }else {
                            finish();
                        }
                    } else {
                        ToastUtils.showToast(SignInActivity.this, message);
                        upDataCode();
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

    //刷新验证码
    private void upDataCode() {
        //重新初始化验证码
        rightCode = iv_signin_code.getValidataAndSetImage();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //判断输入信息是否符合要求 如果符合则登录按钮正常 不符合登录按钮透明度变浅
        if (isMobileNO(et_signin_phone.getText().toString().trim())) {
            phonePass = true;
        } else {
            phonePass = false;
        }
        if (et_signin_password.getText().toString().trim().length() >= 6 && et_signin_password.getText().toString().trim().length() <= 15) {
            passwordPass = true;
        } else {
            passwordPass = false;
        }
        if (et_signin_code.getText().toString().trim().length() == 4) {
            codePass = true;
        } else {
            codePass = false;
        }
        if (phonePass && passwordPass && codePass) {
            tv_signin.setAlpha(1);
        } else {
            tv_signin.setAlpha(0.6f);
        }
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
