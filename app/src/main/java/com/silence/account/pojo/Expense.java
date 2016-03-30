package com.silence.account.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Silence on 2016/3/7 0007.
 * 支出实体类
 */
@DatabaseTable(tableName = "expense")
public class Expense implements Parcelable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int mId;              //主键
    @DatabaseField(canBeNull = false, columnName = "date")
    private Date mDate;         //日期
    @DatabaseField(canBeNull = false, columnName = "amount")
    private float mAmount;        //金额
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "categoryId")
    private ExpenseCat mCategory;     //类别
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "userId")
    private User mUser;             //用户
    @DatabaseField(columnName = "note")
    private String mNote;         //备注

    public Expense() {
    }

    public Expense(Date date, float amount, ExpenseCat category, String note) {
        mDate = date;
        mAmount = amount;
        mCategory = category;
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

    public ExpenseCat getCategory() {
        return mCategory;
    }

    public void setCategory(ExpenseCat category) {
        mCategory = category;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
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
        dest.writeParcelable(this.mCategory, 0);
        dest.writeParcelable(this.mUser, 0);
        dest.writeString(this.mNote);
    }

    protected Expense(Parcel in) {
        this.mId = in.readInt();
        long tmpMDate = in.readLong();
        this.mDate = tmpMDate == -1 ? null : new Date(tmpMDate);
        this.mAmount = in.readFloat();
        this.mCategory = in.readParcelable(ExpenseCat.class.getClassLoader());
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.mNote = in.readString();
    }

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {
        public Expense createFromParcel(Parcel source) {
            return new Expense(source);
        }

        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @Override
    public String toString() {
        return "Expense{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mAmount=" + mAmount +
                ", mCategory=" + mCategory.getName() +
                ", mUser=" + mUser.getName() +
                ", mNote='" + mNote + '\'' +
                '}';
    }
}
