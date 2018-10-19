package com.jsxl.selfexam.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jsxl.selfexam.R;

/**
 * Created by Charles on 2018/7/16.
 */

public class EditNoteDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private EditText et_note_content;
    private String noteContent;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    public EditNoteDialog(Context context) {
        super(context, R.style.MyDialog);
    }
    public void setMessage(String noteContent){
        this.noteContent = noteContent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note_dialog_layout);
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
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick(et_note_content.getText().toString().trim());
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        if(noteContent!=null){
            et_note_content.setText(noteContent);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        et_note_content = (EditText) findViewById(R.id.et_note_content);
    }
    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick(String content);
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
}