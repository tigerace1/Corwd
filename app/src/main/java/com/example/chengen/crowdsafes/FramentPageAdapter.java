package com.example.chengen.crowdsafes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FramentPageAdapter extends FragmentPagerAdapter{
    List<Fragment>fragmentList;
    public FramentPageAdapter(FragmentManager fm,List<Fragment>fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    @Override
    public int getCount() {
        return fragmentList.size();
    }
 }
