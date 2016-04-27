package com.silence.account.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Silence on 2016/4/13 0013.
 * 投资实体类
 */
@DatabaseTable(tableName = "ASinvest")
public class Invest {
    @DatabaseField(generatedId = true, columnName = "ASid")
    private int mId;        //主键
    @DatabaseField(columnName = "ASamount")
    private float mAmount;  //金额
    @DatabaseField(columnName = "ASyear")
    private int mYear;      //期限
    @DatabaseField(columnName = "ASrate")
    private float mRate;    //收益率
    @DatabaseField(columnName = "AStype")
    private String mType;   //类型
    @DatabaseField(columnName = "ASearning")
    private float mEarning; //收益
    @DatabaseField(columnName = "ASdate")
    private Date mDate;     //日期
    @DatabaseField(columnName = "ASuserId", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private User mUser;     //用户

    public Invest() {
    }

    public Invest( float amount, int year, float rate, String type, float earning, Date date, User user) {
        mAmount = amount;
        mYear = year;
        mRate = rate;
        mType = type;
        mEarning = earning;
        mDate = date;
        mUser = user;
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

    public float getAmount() {
        return mAmount;
    }

    public void setAmount(float amount) {
        mAmount = amount;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public float getRate() {
        return mRate;
    }

    public void setRate(float rate) {
        mRate = rate;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public float getEarning() {
        return mEarning;
    }

    public void setEarning(float earning) {
        mEarning = earning;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
