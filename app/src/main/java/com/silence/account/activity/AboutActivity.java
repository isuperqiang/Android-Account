package com.silence.account.activity;

import android.os.Bundle;

import com.silence.account.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("关于我们");
        showDivider(true);
        showBackwardView(true);
    }
}
