package com.jsxl.selfexam.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsxl.selfexam.R;

import static com.jsxl.selfexam.R.id.status_view;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultationFragment extends Fragment {
    private View view;
    private WebView webView;
    private RelativeLayout loading;
    private View title_status;
    public ConsultationFragment() {
        // Required empty public constructor
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consultation, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
        return view;
    }

    private void initData() {
        // TODO Auto-generated method stub
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) title_status.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        linearParams.height = getStatusBarHeight(getActivity());// 控件的宽强制设成30
        title_status.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("http://looyuoms2431.looyu.com/chat/chat/p.do?g=10069299&md=2&c=20002371&v=fc44677b36b3f2ebb6f28d057daa2dc366&u=f85651855f176fd7086537b520dcafcd77&f=10072497&site=0&p0=http%3A%2F%2Fwww.jsxlmed.com%2Fhtml%2Findex.html&ct=54&lang=sc&refer=&loc=http%3A%2F%2Fwww.jsxlmed.com%2Fhtml%2Findex.html%3BJSESSIONID%3Dafb4a427-8d1f-4e87-bb32-53bba905ffe9&_d=1527758219447");
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

    private void initView() {
        loading = (RelativeLayout) view.findViewById(R.id.loading);
        webView = (WebView) view.findViewById(R.id.webView);
        title_status = view.findViewById(R.id.title_status);
    }

}
