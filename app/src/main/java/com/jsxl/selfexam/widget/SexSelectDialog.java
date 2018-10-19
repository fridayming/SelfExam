package com.jsxl.selfexam.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;


import com.jsxl.selfexam.R;

/**
 * Created by Charles on 2018/6/25.
 */

public class SexSelectDialog extends Dialog {
    private CheckBox cb_men,cb_women;
    private RelativeLayout rl_sex_men,rl_sex_women;

    private SexSelectDialog.onSexSelectListener onSexSelectListener;
    private int gender;


    public SexSelectDialog(Context context, SexSelectDialog.onSexSelectListener onSexSelectListener,int gender) {
        super(context, R.style.MyDialog);
        this.onSexSelectListener = onSexSelectListener;
        this.gender = gender;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sex_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        rl_sex_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSexSelectListener.onSexSelect(0);
            }
        });

        //设置取消按钮被点击后，向外界提供监听
        rl_sex_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onSexSelectListener.onSexSelect(1);
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        if(gender == 0){
            cb_men.setChecked(true);
            cb_women.setChecked(false);
        }if(gender == 1){
            cb_men.setChecked(false);
            cb_women.setChecked(true);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        cb_men = (CheckBox) findViewById(R.id.cb_men);
        cb_women = (CheckBox) findViewById(R.id.cb_women);

        rl_sex_men = (RelativeLayout) findViewById(R.id.rl_sex_men);
        rl_sex_women = (RelativeLayout) findViewById(R.id.rl_sex_women);
    }
    public interface onSexSelectListener{
        void onSexSelect(int sex);
    }
}