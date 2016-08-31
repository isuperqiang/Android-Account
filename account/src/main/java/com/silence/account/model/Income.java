package com.silence.account.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Silence on 2016/3/7 0007.
 * 收入实体类
 */
@DatabaseTable(tableName = "ASincome")
public class Income implements Parcelable {
    @DatabaseField(generatedId = true, columnName = "ASid")
    private int mId;              //主键
    @DatabaseField(canBeNull = false, columnName = "ASdate")
    private Date mDate;         //日期
    @DatabaseField(canBeNull = false, columnName = "ASamount")
    private float mAmount;        //金额
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "AScategoryId")
    private IncomeCat mCategory;     //类别
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "ASuserId")
    private User mUser;             //用户
    @DatabaseField(columnName = "ASnote")
    private String mNote;         //备注

    public Income() {

    }

    public Income(Date date, float amount, IncomeCat category, User user, String note) {
        mDate = date;
        mAmount = amount;
        mCategory = category;
        mUser = user;
        mNote = note;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public float getAmount() {
        return mAmount;
    }

    public void setAmount(float amount) {
        mAmount = amount;
    }

    public IncomeCat getCategory() {
        return mCategory;
    }

    public void setCategory(IncomeCat category) {
        mCategory = category;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    @Override
    public String toString() {
        return "Income{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mAmount=" + mAmount +
                ", mCategory=" + mCategory.getName() +
                ", mUser=" + mUser.getName() +
                ", mNote='" + mNote + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeLong(mDate != null ? mDate.getTime() : -1);
        dest.writeFloat(this.mAmount);
        dest.writeParcelable(this.mCategory, flags);
        dest.writeParcelable(this.mUser, flags);
        dest.writeString(this.mNote);
    }

    protected Income(Parcel in) {
        this.mId = in.readInt();
        long tmpMDate = in.readLong();
        this.mDate = tmpMDate == -1 ? null : new Date(tmpMDate);
        this.mAmount = in.readFloat();
        this.mCategory = in.readParcelable(IncomeCat.class.getClassLoader());
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.mNote = in.readString();
    }

    public static final Parcelable.Creator<Income> CREATOR = new Parcelable.Creator<Income>() {
        public Income createFromParcel(Parcel source) {
            return new Income(source);
        }

        public Income[] newArray(int size) {
            return new Income[size];
        }
    };
}