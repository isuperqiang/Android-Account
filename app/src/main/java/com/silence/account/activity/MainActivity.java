package com.silence.account.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.silence.account.R;
import com.silence.account.application.AppApplication;
import com.silence.account.dao.UserDao;
import com.silence.account.fragment.AccountFragment;
import com.silence.account.fragment.DetailFragment;
import com.silence.account.fragment.LineFragment;
import com.silence.account.fragment.MineFragment;
import com.silence.account.fragment.PieFragment;
import com.silence.account.utils.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.radio_gp_tab)
    RadioGroup mRadioGpTab;
    private long exitTime;
    private FragmentManager mFragmentManager;
    private AccountFragment mAccountFragment;
    private DetailFragment mDetailFragment;
    private MineFragment mMineFragment;
    private PieFragment mPieFragment;
    private LineFragment mLineFragment;
    private int mChartType;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        mRadioGpTab.setOnCheckedChangeListener(this);
        AppApplication.sUser = new UserDao(this).getCurrentUser(BmobUser.getCurrentUser(getApplicationContext()).getUsername());
        mFragmentManager = getSupportFragmentManager();
        ((RadioButton) (mRadioGpTab.getChildAt(0))).setChecked(true);
        registerReceiver(mBroadcastReceiver, new IntentFilter(Constant.INTENT_FILTER));
        mChartType = R.id.radio_pop_pie;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private void switchFragment(Fragment newFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!newFragment.isAdded()) {
            transaction.add(R.id.main_fragment, newFragment);
        }
        if (mDetailFragment != null && mDetailFragment.isVisible()) {
            transaction.hide(mDetailFragment);
        }
        if (mAccountFragment != null && mAccountFragment.isVisible()) {
            transaction.hide(mAccountFragment);
        }
        if (mMineFragment != null && mMineFragment.isVisible()) {
            transaction.hide(mMineFragment);
        }
        if (mPieFragment != null && mPieFragment.isVisible()) {
            transaction.hide(mPieFragment);
        }
        if (mLineFragment != null && mLineFragment.isVisible()) {
            transaction.hide(mLineFragment);
        }
        transaction.show(newFragment);
        transaction.commit();
    }

    private void switchTitleBar(int title, boolean showBackward, boolean showForward, int forwardResId) {
        setTitle(title);
        if (showBackward) {
            showBackwardView(true);
        } else {
            showBackwardView(false);
        }
        if (showForward) {
            if (forwardResId == 0) {
                showForwardView(true);
            } else {
                showForwardView(true, forwardResId);
            }
        } else {
            showForwardView(false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.radio_gp_tab) {
            switch (checkedId) {
                case R.id.radio_btn_detail:
                    if (mDetailFragment == null) {
                        mDetailFragment = new DetailFragment();
                    }
                    switchTitleBar(R.string.tab_detail, false, false, 0);
                    switchFragment(mDetailFragment);
                    break;
                case R.id.radio_btn_account:
                    if (mAccountFragment == null) {
                        mAccountFragment = new AccountFragment();
                    }
                    switchTitleBar(R.string.tab_invest, false, false, 0);
                    switchFragment(mAccountFragment);
                    break;
                case R.id.radio_btn_chart:
                    switchChart(mChartType);
                    switchTitleBar(R.string.tab_chart, false, true, R.mipmap.ic_expand_more);
                    break;
                case R.id.radio_btn_me:
                    if (mMineFragment == null) {
                        mMineFragment = new MineFragment();
                    }
                    switchTitleBar(R.string.tab_mine, false, false, 0);
                    switchFragment(mMineFragment);
                    break;
            }
        } else {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            mChartType = checkedId;
            switchChart(checkedId);
        }
    }

    @Override
    protected void onForward() {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(mRadioGpTab, Gravity.BOTTOM, 0, 0);
            showForwardView(true, R.mipmap.ic_arrow_up);
        }
    }

    private void initPopWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_chart_type, null);
        RadioGroup radioGroup = (RadioGroup) popView.findViewById(R.id.radio_group_chart);
        radioGroup.setOnCheckedChangeListener(this);
        mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showForwardView(true, R.mipmap.ic_expand_more);
            }
        });
    }

    private void switchChart(int checkId) {
        if (checkId == R.id.radio_pop_pie) {
            if (mPieFragment == null) {
                mPieFragment = new PieFragment();
                initPopWindow();
            }
            switchFragment(mPieFragment);
        } else {
            if (mLineFragment == null) {
                mLineFragment = new LineFragment();
            }
            switchFragment(mLineFragment);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(getApplicationContext(), R.string.text_exit, Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        ShareSDK.stopSDK(this);
    }

}
