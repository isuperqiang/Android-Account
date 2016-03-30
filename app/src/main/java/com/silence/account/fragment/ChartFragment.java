package com.silence.account.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.silence.account.R;
import com.silence.account.activity.MainActivity;
import com.silence.account.adapter.PagerAdapter;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Silence on 2016/3/7 0007.
 */
public class ChartFragment extends BaseFragment {

    @Bind(R.id.chart_tab_strip)
    PagerSlidingTabStrip mChartTabStrip;
    @Bind(R.id.pager_chart)
    ViewPager mPagerChart;
    private MainActivity mMainActivity;
    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);
        List<Fragment> fragments = new ArrayList<>(2);
        PieExpenseFgt pieExpenseFgt = new PieExpenseFgt();
        PieIncomeFgt pieIncomeFgt = new PieIncomeFgt();
        fragments.add(pieIncomeFgt);
        fragments.add(pieExpenseFgt);
        mPagerAdapter = new PagerAdapter(mMainActivity.getSupportFragmentManager(), fragments);
        mPagerChart.setAdapter(mPagerAdapter);
        mChartTabStrip.setViewPager(mPagerChart);
        mChartTabStrip.setTextSize(ScreenUtils.dp2sp(mMainActivity, 16));
        return view;
    }

    public void refreshChart() {
        int currentItem = mPagerChart.getCurrentItem();
        if (currentItem == 0) {
            PieIncomeFgt pieIncomeFgt = (PieIncomeFgt) mPagerAdapter.getItem(currentItem);
            pieIncomeFgt.updateData(DateUtils.getMonthStart(), DateUtils.getMonthEnd());
        } else {
            PieExpenseFgt pieExpenseFgt = (PieExpenseFgt) mPagerAdapter.getItem(currentItem);
            pieExpenseFgt.updateData(DateUtils.getMonthStart(), DateUtils.getMonthEnd());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}