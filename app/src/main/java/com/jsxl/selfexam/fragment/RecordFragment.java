package com.jsxl.selfexam.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsxl.selfexam.R;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_record_test;
    private TextView tv_record_exam;
    private View record_line1, record_line2;
    private RecordDetailFragment recordDetailFragment1, recordDetailFragment2;
    private View status_view;

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_record, container, false);
        initView();
        initData();
        return view;
    }

    private void initView() {
        tv_record_test = (TextView) view.findViewById(R.id.tv_record_test);
        tv_record_test.setOnClickListener(this);
        tv_record_exam = (TextView) view.findViewById(R.id.tv_record_exam);
        tv_record_exam.setOnClickListener(this);
        record_line1 = view.findViewById(R.id.record_line1);
        record_line2 = view.findViewById(R.id.record_line2);
        status_view = view.findViewById(R.id.status_view);
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    private void initData() {
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) status_view.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        linearParams.height = getStatusBarHeight(getActivity());// 控件的宽强制设成30
        status_view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
        tv_record_test.setTextColor(Color.parseColor("#4397f7"));
        tv_record_exam.setTextColor(Color.parseColor("#333333"));
        record_line1.setVisibility(View.VISIBLE);
        record_line2.setVisibility(View.INVISIBLE);
        recordDetailFragment1 = new RecordDetailFragment(0);
        recordDetailFragment2 = new RecordDetailFragment(1);
        transaction1.add(R.id.record_fragment_container, recordDetailFragment1);
        transaction1.add(R.id.record_fragment_container, recordDetailFragment2);
        transaction1.show(recordDetailFragment1).hide(recordDetailFragment2);
        transaction1.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.tv_record_test:
                tv_record_test.setTextColor(Color.parseColor("#4397f7"));
                tv_record_exam.setTextColor(Color.parseColor("#333333"));
                record_line1.setVisibility(View.VISIBLE);
                record_line2.setVisibility(View.INVISIBLE);
                transaction1.show(recordDetailFragment1);
                transaction1.hide(recordDetailFragment2);
                break;
            case R.id.tv_record_exam:
                tv_record_test.setTextColor(Color.parseColor("#333333"));
                tv_record_exam.setTextColor(Color.parseColor("#4397f7"));
                record_line1.setVisibility(View.INVISIBLE);
                record_line2.setVisibility(View.VISIBLE);
                transaction1.show(recordDetailFragment2);
                transaction1.hide(recordDetailFragment1);
                break;
        }
        transaction1.commit();

    }
}
