package com.silence.account.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.silence.account.R;
import com.silence.account.application.AppApplication;
import com.silence.account.dao.UserDao;
import com.silence.account.fragment.AccountFragment;
import com.silence.account.fragment.ChartFragment;
import com.silence.account.fragment.DetailFragment;
import com.silence.account.fragment.MineFragment;
import com.silence.account.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.radio_gp_tab)
    RadioGroup mRadioGpTab;
    private long exitTime;
    private FragmentManager mFragmentManager;
    private AccountFragment mAccountFragment;
    private ChartFragment mChartFragment;
    private DetailFragment mDetailFragment;
    private MineFragment mMineFragment;
    private List<Fragment> mFragmentList;

    @Override
    public void initView() {
        disPlayBack(false);
        setActionTitle("记账");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRadioGpTab.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppApplication.setUser(new UserDao(this).getCurrentUser(BmobUser.getCurrentUser(getApplicationContext()).getUsername()));
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>(4);
        ((RadioButton) (mRadioGpTab.getChildAt(0))).setChecked(true);
        registerReceiver(mBroadcastReceiver, new IntentFilter(Constant.INTENT_FILTER));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private void hideAllFgt(FragmentTransaction transaction) {
        if (mFragmentList != null && mFragmentList.size() > 0) {
            for (int i = 0; i < mFragmentList.size(); i++) {
                if (mFragmentList.get(i).isVisible()) {
                    transaction.hide(mFragmentList.get(i));
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAllFgt(transaction);
        switch (checkedId) {
            case R.id.radio_btn_detail:
                if (mDetailFragment == null) {
                    mDetailFragment = new DetailFragment();
                    mFragmentList.add(mDetailFragment);
                    transaction.add(R.id.main_fragment, mDetailFragment);
                } else {
                    transaction.show(mDetailFragment);
                }
                break;
            case R.id.radio_btn_account:
                if (mAccountFragment == null) {
                    mAccountFragment = new AccountFragment();
                    mFragmentList.add(mAccountFragment);
                    transaction.add(R.id.main_fragment, mAccountFragment);
                } else {
                    transaction.show(mAccountFragment);
                }
                break;
            case R.id.radio_btn_chart:
                if (mChartFragment == null) {
                    mChartFragment = new ChartFragment();
                    mFragmentList.add(mChartFragment);
                    transaction.add(R.id.main_fragment, mChartFragment);
                } else {
                    mChartFragment.refreshChart();
                    transaction.show(mChartFragment);
                }
                break;
            case R.id.radio_btn_me:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    mFragmentList.add(mMineFragment);
                    transaction.add(R.id.main_fragment, mMineFragment);
                } else {
                    transaction.show(mMineFragment);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
