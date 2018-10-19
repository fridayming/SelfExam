package com.jsxl.selfexam.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsxl.selfexam.R;

/**
 * Created by Charles on 2018/7/5.
 */

public class ArticleContentDialog extends Dialog {


    private TextView tv_article_content;//消息提示文本
    private ImageView iv_back;
    private String message;
    private ArticleContentDialog.onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(ArticleContentDialog.onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }


    public ArticleContentDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_content_dialog);
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
        //设置取消按钮被点击后，向外界提供监听
        iv_back.setOnClickListener(new View.OnClickListener() {
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
        //如果用户自定了title和message
//        if (titleStr != null) {
//            titleTv.setText(titleStr);
//        }
        if (message != null) {
            tv_article_content.setText(message);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_article_content = (TextView) findViewById(R.id.tv_article_content);
    }


    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
}