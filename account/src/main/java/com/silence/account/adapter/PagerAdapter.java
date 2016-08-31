package com.silence.account.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Silence on 2016/3/23 0023.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private FragmentManager mFragmentManager;
    private boolean[] mRefresh = {false, false};

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
        mFragmentManager = fm;
    }

    public void setRefresh(boolean[] refresh) {
        mRefresh = refresh;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if (mRefresh[position]) {
            String tag = fragment.getTag();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragment);
            fragment = mFragments.get(position);
            ft.add(container.getId(), fragment, tag);
            ft.attach(fragment);
            ft.commit();
            mRefresh[position] = false;
        }
        return fragment;
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
