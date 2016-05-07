package com.silence.account.utils;

/**
 * Created by Silence on 2016/3/6 0006.
 */
public interface Constant {
    int DELAY_TIME=1000;
    String FORGET_PASS = "com.silence.forget.password";
    String TYPE_CATEGORY = "type_category";
    String TYPE_INCOME = "com.silence.pojo.income";
    String TYPE_EXPENSE = "com.silence.pojo.expense";
    int TYPE_TODAY = 0x1;
    int TYPE_WEEK = 0x2;
    int TYPE_MONTH = 0x3;
    String TYPE_DATE = "date type";
    String RECORD = "modify record";
    int REQUEST_UPDATE_FINANCE = 0X100;
    String NEW_USERNAME="new_user_name";
    String NEW_FILENAME="new_file_name";
    String UPDATE_CAT = "request_update_category";
    String INTENT_FILTER="com.silence.account.broadcast";
    String INVEST="com.silence.pojo.invset";
    String ACTION_ALARM="com.silence.account.receiver";
}
