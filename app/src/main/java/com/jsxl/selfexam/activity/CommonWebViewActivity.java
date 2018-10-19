package com.jsxl.selfexam.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.ContactURL;

public class CommonWebViewActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private WebView webView;
    private RelativeLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web_view);
        setStatusColor();
        initView();
        initData();
    }

    public void initData() {
        tv_title.setText("联系我们");
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);

        webView.loadUrl(ContactURL.WITH_US);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loading.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        webView = (WebView) findViewById(R.id.webView);
        loading = (RelativeLayout) findViewById(R.id.loading);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        int id = arg0.getId();
        switch (id) {
            case R.id.iv_back:
                this.finish();
                break;
            default:
                break;
        }
    }
}
