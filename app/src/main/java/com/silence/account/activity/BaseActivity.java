package com.silence.account.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Silence on 2015/8/27 0027.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public ActionBar mActionBar;

    public abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RefWatcher refWatcher = AppApplication.getRefWatcher(this);
//        refWatcher.watch(this);
        mActionBar = getSupportActionBar();
        initView();
    }

    public void disPlayBack(boolean visible) {
        if (visible) {
            if (mActionBar != null) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    public void setActionTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

}
