package com.jsxl.selfexam.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.activity.ChapterActivity;
import com.jsxl.selfexam.activity.CollectListActivity;
import com.jsxl.selfexam.activity.QuestionActivity;
import com.jsxl.selfexam.activity.SignInActivity;
import com.jsxl.selfexam.adapter.HomeFragmentAdapter;
import com.jsxl.selfexam.utils.ToastUtils;
import com.jsxl.selfexam.widget.CircleProgressView;
import com.jsxl.selfexam.widget.ViewPagerForScrollView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RelativeLayout home_rl_bg;
    private CircleProgressView cp_progress;
    private TextView home_tv_progress, home_tv_address;
    private ImageView home_iv_collection, home_iv_error, home_iv_note;
    private TextView home_tv_collection, home_tv_error, home_tv_note;
    private TabLayout home_tablayout;
    private ViewPagerForScrollView home_viewpager;
    private List<String> nameList;
    private ArrayList<Fragment> fragments;
    private SharedPreferences sp;
    private String chapterName,subjectName;
    private int subjectId,chapterId,chapterPaperType;
    private int progressTotal,progressNow;
    private String examId;
    private TestListFragment leftFragment,rightFragment;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        sp = getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        examId = sp.getString("examId","");
        initView();
        initData();
        return view;
    }


    private void initView() {
        home_rl_bg = (RelativeLayout) view.findViewById(R.id.home_rl_bg);
        home_rl_bg.setOnClickListener(this);
        cp_progress = (CircleProgressView) view.findViewById(R.id.cp_progress);
        home_tv_progress = (TextView) view.findViewById(R.id.home_tv_progress);
        home_tv_address = (TextView) view.findViewById(R.id.home_tv_address);
        home_iv_collection = (ImageView) view.findViewById(R.id.home_iv_collection);
        home_iv_collection.setOnClickListener(this);
        home_iv_error = (ImageView) view.findViewById(R.id.home_iv_error);
        home_iv_error.setOnClickListener(this);
        home_iv_note = (ImageView) view.findViewById(R.id.home_iv_note);
        home_iv_note.setOnClickListener(this);
        home_tv_collection = (TextView) view.findViewById(R.id.home_tv_collection);
        home_tv_collection.setOnClickListener(this);
        home_tv_error = (TextView) view.findViewById(R.id.home_tv_error);
        home_tv_error.setOnClickListener(this);
        home_tv_note = (TextView) view.findViewById(R.id.home_tv_note);
        home_tv_note.setOnClickListener(this);
        home_tablayout = (TabLayout) view.findViewById(R.id.home_tablayout);
        home_viewpager = (ViewPagerForScrollView) view.findViewById(R.id.home_viewpager);

    }

    @Override
    public void onResume() {
        super.onResume();
        progressTotal = sp.getInt("progressTotal",0);
        progressNow = sp.getInt("progressNow",0);
        subjectName = sp.getString("subjectName","");
        chapterName = sp.getString("chapterName","");
        subjectId = sp.getInt("subjectId",0);
        chapterId = sp.getInt("chapterId",0);
        chapterPaperType = sp.getInt("chapterPaperType",0);
        cp_progress.setProgress(progressTotal==0?0:progressNow*100/progressTotal);
        home_tv_progress.setText("上次练习进度"+progressNow+"/"+progressTotal);
        if(subjectName.length()==0){
            home_tv_address.setText("暂无做题进度");
        }
        if(subjectName!=null&&subjectName.length()>0) {
            home_tv_address.setText(subjectName + ">" + chapterName);
        }else{
            home_tv_address.setText("暂无做题进度");
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if(examId.equals(sp.getString("examId","0"))){
                return;
            }else{
                leftFragment.uploadDate();
                rightFragment.uploadDate();
                examId = sp.getString("examId","0");
            }
        }
    }
    private void initData() {
        leftFragment = new TestListFragment(0);
        rightFragment = new TestListFragment(1);
        nameList = new ArrayList<>();
        nameList.add("章节练习");
        nameList.add("模拟试卷");
        fragments = new ArrayList<>();
        fragments.add(leftFragment);
        fragments.add(rightFragment);
        // 创建ViewPager适配器
        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(getActivity().getSupportFragmentManager());
        homeFragmentAdapter.setFragments(fragments, nameList);
        // 给ViewPager设置适配器
        home_viewpager.setAdapter(homeFragmentAdapter);
        // TabLayout 指示器 (记得自己手动创建4个Fragment,注意是 app包下的Fragment 还是 V4包下的 Fragment)
        home_tablayout.addTab(home_tablayout.newTab().setText("章节练习"));
        home_tablayout.addTab(home_tablayout.newTab().setText("模拟试卷"));
        // 使用 TabLayout 和 ViewPager 相关联
        home_tablayout.setupWithViewPager(home_viewpager);
        //tablelayout添加竖线
        LinearLayout linearLayout = (LinearLayout) home_tablayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.layout_divider_vertical));
        doRequestByRetrofit();
    }


    private void doRequestByRetrofit() {
//        Retrofit retrofit2 = new Retrofit.Builder()
//                .baseUrl(ContactURL.baseUrl)//基础URL 建议以 / 结尾
//                .addConverterFactory(ScalarsConverterFactory.create())//设置 Json 转换器
//                .build();
//        RetrofitService retrofitService2 = retrofit2.create(RetrofitService.class);
//        Call<String> call2 = retrofitService2.getStringSubject("1");
//        call2.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                //测试数据返回
//                Toast.makeText(getActivity(), response.body(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(getActivity(), "cuowu", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
            boolean isLogin = sp.getBoolean("isLogin",false);
            Intent intent = null;
        switch (v.getId()) {
            case R.id.home_rl_bg:
                intent = new Intent(getActivity(),QuestionActivity.class);
                intent.putExtra("chapterName",chapterName);
                intent.putExtra("chapterId",chapterId);
                intent.putExtra("chapterPaperType",chapterPaperType);
                intent.putExtra("subjectId",subjectId);
                intent.putExtra("subjectName",subjectName);
                intent.putExtra("progressNow",progressNow);
                intent.putExtra("from","report");
                startActivity(intent);
                break ;
            case R.id.home_iv_collection:
            case R.id.home_tv_collection:
                if(isLogin) {
                    intent = new Intent(getActivity(), CollectListActivity.class);
                    intent.putExtra("type", "collect");
                    startActivity(intent);
                }else{
                    intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.home_iv_error:
            case R.id.home_tv_error:
                if(isLogin) {
                    intent = new Intent(getActivity(), CollectListActivity.class);
                    intent.putExtra("type", "error");
                    startActivity(intent);
                }else{
                    intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.home_iv_note:
            case R.id.home_tv_note:
                if(isLogin) {
                    intent = new Intent(getActivity(), CollectListActivity.class);
                    intent.putExtra("type", "note");
                    startActivity(intent);
                }else{
                    intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
