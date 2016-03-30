package com.silence.account.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Silence on 2016/3/23 0023.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "收入" : "支出";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
