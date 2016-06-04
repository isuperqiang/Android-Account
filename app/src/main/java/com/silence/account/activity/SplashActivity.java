package com.silence.account.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.silence.account.R;
import com.silence.account.utils.Constant;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Intent intent = new Intent();
        if (BmobUser.getCurrentUser(this) != null) {
            //当前用户登录过，系统有缓存，设置跳转页面为主界面
            intent.setClass(this, MainActivity.class);
        } else {
            //当前用户未登录，设置跳转页面为登录页
            intent.setClass(this, LoginActivity.class);
        }
        //延迟一秒钟加载新的窗口
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //开启新的界面
                startActivity(intent);
                //添加渐变的过渡动画
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //结束当前启动页
                finish();
            }
        }, Constant.DELAY_TIME);
    }
}