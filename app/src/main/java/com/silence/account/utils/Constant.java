package com.silence.account.utils;

/**
 * Created by Silence on 2016/3/6 0006.
 */
public interface Constant {
    String FORGET_PASS = "com.silence.forget.password";
    String TYPE_ADD_CATEGORY = "com.silence.pojo.category";
    String TYPE_CATEGORY = "type_category";
    String TYPE_INCOME = "com.silence.pojo.income";
    String TYPE_EXPENSE = "com.silence.pojo.expense";
    int TYPE_TODAY = 0x1;
    int TYPE_WEEK = 0x2;
    int TYPE_MONTH = 0x3;
    String TYPE_DATE = "date type";
    String RECORD = "modify record";
    int REQUEST_UPDATE_FINANCE = 0X100;
    int RESULT_INSERT_FINANCE = 0X101;
    int RESULT_UPDATE_FINANCE = 0X102;
    int RESULT_REFRESH_FINANCE = 0X103;
    String NEW_USERNAME="new_user_name";
    String NEW_FILENAME="new_file_name";
    String UPDATE_CAT = "request_update_category";
    String INTENT_FILTER="com.silence.account.broadcast";
}
