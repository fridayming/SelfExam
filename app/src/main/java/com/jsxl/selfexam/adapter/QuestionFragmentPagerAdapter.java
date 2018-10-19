package com.jsxl.selfexam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Charles on 2018/6/28.
 */

public class QuestionFragmentPagerAdapter  extends FragmentStatePagerAdapter  {
    private FragmentManager mfragmentManager;
    private List<Fragment> mlist;

    public QuestionFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mlist = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }
}
