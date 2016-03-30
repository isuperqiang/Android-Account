package com.silence.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.silence.account.R;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends BaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent();
        if (BmobUser.getCurrentUser(this) != null) {
            intent.setClass(this, MainActivity.class);
        } else {
            intent.setClass(this, LoginActivity.class);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 1000);
    }
}