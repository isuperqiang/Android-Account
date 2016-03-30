package com.silence.account.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Silence on 2016/3/7 0007.
 * 账户实体类
 */
@DatabaseTable(tableName = "account")
public class Account {
    @DatabaseField(generatedId = true, columnName = "id")
    private int mId;          //主键
    @DatabaseField(canBeNull = false, columnName = "name")
    private String mName;     //名称
    @DatabaseField(canBeNull = false, columnName = "type")
    private String mType;     //类型
    @DatabaseField(canBeNull = false, columnName = "amount")
    private float mAmount;    //金额
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "userId")
    private User mUser;       //用户

    public Account() {
    }

    public Account(String name, String type, float amount) {
        mName = name;
        mType = type;
        mAmount = amount;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public float getAmount() {
        return mAmount;
    }

    public void setAmount(float amount) {
        mAmount = amount;
    }
}
