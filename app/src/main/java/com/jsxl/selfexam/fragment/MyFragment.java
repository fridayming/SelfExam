package com.jsxl.selfexam.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.activity.CommonWebViewActivity;
import com.jsxl.selfexam.activity.ExamTypeActivity;
import com.jsxl.selfexam.activity.FeedBackActivity;
import com.jsxl.selfexam.activity.PersonInfoActivity;
import com.jsxl.selfexam.activity.SettingActivity;
import com.jsxl.selfexam.activity.SignInActivity;
import com.jsxl.selfexam.activity.SystemMessageActivity;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.Circle;
import com.jsxl.selfexam.widget.DrawableTextView;
import com.squareup.picasso.Picasso;

import static com.jsxl.selfexam.R.id.iv_person_head;

public class MyFragment extends Fragment implements View.OnClickListener {
    private View view;
    //头像  消息
    private ImageView my_iv_head, my_iv_news;
    private TextView my_tv_head;
    private RelativeLayout rl_exam_type, rl_feedback, rl_contact_us, rl_set;
    private DrawableTextView my_exam_name;
    private SharedPreferences sp;
    private boolean isLogin;
    private SignSuccessPageChangeListener listener;
    //监听登陆成功 通知Activity跳转到首页
    public interface SignSuccessPageChangeListener{
        void pageChangeListener();
    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public MyFragment() {
    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public MyFragment(SignSuccessPageChangeListener listener) {
        // Required empty public constructor
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my, container, false);
        sp = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        initView();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin = sp.getBoolean("isLogin", false);
        my_exam_name.setText(sp.getString("examName",""));
        if(isLogin){
            my_tv_head.setText(sp.getString("userName",""));
            Picasso.with(getActivity()).load(sp.getString("headUrl","0")).transform(new Circle()).error(R.drawable.icon_default_head).into(my_iv_head);
        }else{
            my_tv_head.setText("点击头像登陆");
            my_iv_head.setImageResource(R.drawable.icon_default_head);
        }
    }

    private void initView() {
        my_iv_head = (ImageView) view.findViewById(R.id.my_iv_head);
        my_iv_head.setOnClickListener(this);
        my_iv_news = (ImageView) view.findViewById(R.id.my_iv_news);
        my_iv_news.setOnClickListener(this);
        my_tv_head = (TextView) view.findViewById(R.id.my_tv_head);
        my_tv_head.setOnClickListener(this);
        my_exam_name = (DrawableTextView) view.findViewById(R.id.my_exam_name);
        rl_exam_type = (RelativeLayout) view.findViewById(R.id.rl_exam_type);
        rl_exam_type.setOnClickListener(this);
        rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
        rl_feedback.setOnClickListener(this);
        rl_contact_us = (RelativeLayout) view.findViewById(R.id.rl_contact_us);
        rl_contact_us.setOnClickListener(this);
        rl_set = (RelativeLayout) view.findViewById(R.id.rl_set);
        rl_set.setOnClickListener(this);
    }

    private void initData() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            boolean success = data.getBooleanExtra("success",false);
            if(success) {
                listener.pageChangeListener();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.my_iv_head:
            case R.id.my_tv_head:
                if (isLogin) {
                    intent = new Intent(getActivity(), PersonInfoActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), SignInActivity.class);
                    startActivityForResult(intent,0);
                }
                break;
            case R.id.my_iv_news:
                if(isLogin) {
                    intent = new Intent(getActivity(), SystemMessageActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtils.showToast(getActivity(),"请先登录");
                }
                break;

            case R.id.rl_exam_type:
                if(isLogin) {
                    intent = new Intent(getActivity(), ExamTypeActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtils.showToast(getActivity(),"请先登录");
                }
                break;
            case R.id.rl_feedback:
                intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_contact_us:
                intent = new Intent(getActivity(), CommonWebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_set:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

        }
    }
}
