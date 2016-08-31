package com.silence.account.utils;

/**
 * Created by Silence on 2016/3/6 0006.
 */
public interface Constant {
    int DELAY_TIME=2000;
    String FORGET_PASS = "com.silence.forget.password";
    String TYPE_CATEGORY = "type_category";
    String TYPE_INCOME = "com.silence.pojo.income";
    String TYPE_EXPENSE = "com.silence.pojo.expense";
    int TYPE_TODAY = 0x101;
    int TYPE_WEEK = 0x202;
    int TYPE_MONTH = 0x303;
    String TYPE_DATE = "date_type";
    String RECORD = "modify_record";
    int REQUEST_UPDATE_FINANCE = 0X100;
    String NEW_USERNAME="new_user_name";
    String NEW_FILENAME="new_file_name";
    String UPDATE_CAT = "request_update_category";
    String ACTION_FINISH ="silence.intent.action.finish";
    String INVEST="com.silence.pojo.invset";
    String ACTION_ALARM="silence.intent.action.alarm";
}
