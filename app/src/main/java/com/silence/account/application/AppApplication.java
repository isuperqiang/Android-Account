package com.silence.account.application;

import android.app.Application;

import com.silence.account.pojo.User;

import cn.bmob.v3.Bmob;

/**
 * Created by Silence on 2016/3/28 0028.
 */
public class AppApplication extends Application {
//    private RefWatcher refWatcher;
    private static AppApplication sAppApplication;
    private static User sUser;

//    public static RefWatcher getRefWatcher(Context context) {
//        AppApplication application = (AppApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

    public static AppApplication getApplication() {
        return sAppApplication;
    }

    public static User getUser() {
        return sUser;
    }

    public static void setUser(User user) {
        sUser = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(), "");
        sAppApplication = this;
//        refWatcher = LeakCanary.install(this);
    }
}
