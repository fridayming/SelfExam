package com.jsxl.selfexam.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsxl.selfexam.R;
import com.jsxl.selfexam.adapter.HomeFragmentAdapter;
import com.jsxl.selfexam.base.BaseActivity;
import com.jsxl.selfexam.base.BaseFragmentActivity;
import com.jsxl.selfexam.fragment.CollectListFragment;
import com.jsxl.selfexam.fragment.TestListFragment;
import com.jsxl.selfexam.widget.ViewPagerForScrollView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.jsxl.selfexam.R.id.home_tablayout;
import static com.jsxl.selfexam.R.id.home_viewpager;

public class CollectListActivity extends BaseFragmentActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_title;
    private TabLayout tl_collect_tablayout;
    private ViewPager vp_collect_viewpager;
    private ArrayList<Fragment> fragments;
    private List<String> nameList;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list);
        setStatusColor();
        type = getIntent().getStringExtra("type");
        initView();
        initData();
    }


    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tl_collect_tablayout = (TabLayout) findViewById(R.id.tl_collect_tablayout);
        vp_collect_viewpager = (ViewPager) findViewById(R.id.vp_collect_viewpager);

    }
    @Override
    public void initData() {
        if(type.equals("collect")){
            tv_title.setText("收藏");
        }else if(type.equals("error")){
            tv_title.setText("错题");
        }else if(type.equals("note")){
            tv_title.setText("笔记");
        }
        nameList = new ArrayList<>();
        nameList.add("章节练习");
        nameList.add("模拟试卷");
        fragments = new ArrayList<>();
        CollectListFragment fragment0 = new CollectListFragment(0,type);
        CollectListFragment fragment1 = new CollectListFragment(1,type);
        fragments.add(fragment0);
        fragments.add(fragment1);
        // 创建ViewPager适配器
        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(this.getSupportFragmentManager());
        homeFragmentAdapter.setFragments(fragments, nameList);
        // 给ViewPager设置适配器
        vp_collect_viewpager.setAdapter(homeFragmentAdapter);
        // TabLayout 指示器 (记得自己手动创建4个Fragment,注意是 app包下的Fragment 还是 V4包下的 Fragment)
        tl_collect_tablayout.addTab(tl_collect_tablayout.newTab().setText("章节练习"));
        tl_collect_tablayout.addTab(tl_collect_tablayout.newTab().setText("模拟试卷"));
        // 使用 TabLayout 和 ViewPager 相关联
        tl_collect_tablayout.setupWithViewPager(vp_collect_viewpager);
        //tablelayout添加竖线
        LinearLayout linearLayout = (LinearLayout) tl_collect_tablayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.layout_divider_vertical));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
