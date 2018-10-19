package com.jsxl.selfexam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Charles on 2018/6/12.
 */

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> nameList;

    public void setFragments(ArrayList<Fragment> fragmentList, List<String> nameList) {
        this.fragmentList = fragmentList;
        this.nameList = nameList;
    }

    public HomeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return nameList.get(position);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = fragmentList.get(position);

        return fragment;
    }

    @Override
    public int getCount() {

        return fragmentList.size();
    }
}
