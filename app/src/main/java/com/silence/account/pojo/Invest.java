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
    private int mId;
    @DatabaseField(columnName = "ASamount")  //金额
    private float mAmount;
    @DatabaseField(columnName = "ASyear")  //期限
    private int mYear;
    @DatabaseField(columnName = "ASrate")  //收益率
    private float mRate;
    @DatabaseField(columnName = "AStype")  //类型
    private String mType;
    @DatabaseField(columnName = "ASearning")  //收益
    private float mEarning;
    @DatabaseField(columnName = "ASdate")  //收益
    private Date mDate;

    public Invest() {
    }

    public Invest(float amount, int year, float rate, String type, float earning, Date date) {
        mAmount = amount;
        mYear = year;
        mRate = rate;
        mType = type;
        mEarning = earning;
        mDate = date;
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
