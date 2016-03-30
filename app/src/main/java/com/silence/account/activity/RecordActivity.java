package com.silence.account.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.silence.account.R;
import com.silence.account.adapter.PagerAdapter;
import com.silence.account.fragment.ExpenseFragment;
import com.silence.account.fragment.IncomeFragment;
import com.silence.account.pojo.Expense;
import com.silence.account.pojo.Income;
import com.silence.account.utils.Constant;
import com.silence.account.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordActivity extends BaseActivity implements IncomeFragment.onTimePickListener,
        ExpenseFragment.onTimePickListener {

    @Bind(R.id.record_tab_strip)
    PagerSlidingTabStrip mRecordTabStrip;
    @Bind(R.id.pager_record)
    ViewPager mPagerRecord;
    private FragmentManager mFragmentManager;
    private IncomeFragment mIncomeFragment;
    private ExpenseFragment mExpenseFragment;

    @Override
    public void initView() {
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        disPlayBack(true);
        setActionTitle("记账");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=");
        mFragmentManager = getSupportFragmentManager();
        Parcelable extra = getIntent().getParcelableExtra(Constant.RECORD);
        int index = 0;
        if (extra != null) {
            if (extra instanceof Income) {
                mIncomeFragment = IncomeFragment.getInstance((Income) extra);
                mExpenseFragment = new ExpenseFragment();
            } else if (extra instanceof Expense) {
                mExpenseFragment = ExpenseFragment.getInstance((Expense) extra);
                mIncomeFragment = new IncomeFragment();
                index = 1;
            }
        } else {
            mExpenseFragment = new ExpenseFragment();
            mIncomeFragment = new IncomeFragment();
        }
        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(mIncomeFragment);
        fragments.add(mExpenseFragment);
        mPagerRecord.setAdapter(new PagerAdapter(mFragmentManager, fragments));
        mPagerRecord.setCurrentItem(index);
        mRecordTabStrip.setViewPager(mPagerRecord);
        mRecordTabStrip.setTextSize(ScreenUtils.dp2sp(this, 16));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_item_save) {
            if (mPagerRecord.getCurrentItem() == 1) {
                mExpenseFragment.saveExpense();
            } else {
                mIncomeFragment.saveIncome();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void DisplayDialog(Date date) {
        SlideDateTimeListener listener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                if (mPagerRecord.getCurrentItem() == 1) {
                    mExpenseFragment.setDate(date);
                } else {
                    mIncomeFragment.setDate(date);
                }
            }

            @Override
            public void onDateTimeCancel() {

            }
        };
        new SlideDateTimePicker.Builder(mFragmentManager)
                .setListener(listener)
                .setInitialDate(date)
                .setIs24HourTime(true)
                .setIndicatorColor(Color.parseColor("#f6a844"))
                .build()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
