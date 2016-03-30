package com.silence.account.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Silence on 2016/3/7 0007.
 * 支出类别类
 */
@DatabaseTable(tableName = "expenseCat")
public class ExpenseCat implements Parcelable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int mId;        //主键
    @DatabaseField(canBeNull = false, columnName = "name")
    private String mName;   //名称
    @DatabaseField(canBeNull = false, columnName = "image")
    private int mImageId;   //图标
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "userId")
    private User mUser;     //用户

    public ExpenseCat() {
    }

    public ExpenseCat(int id, String name, int imageId, User user) {
        mId = id;
        mName = name;
        mImageId = imageId;
        mUser = user;
    }

    public ExpenseCat(int imageId, String name) {
        mImageId = imageId;
        mName = name;
    }

    public ExpenseCat(int imageId, String name, User user) {
        mUser = user;
        mImageId = imageId;
        mName = name;
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

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeInt(this.mImageId);
        dest.writeParcelable(this.mUser, flags);
    }

    protected ExpenseCat(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mImageId = in.readInt();
        this.mUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<ExpenseCat> CREATOR = new Parcelable.Creator<ExpenseCat>() {
        public ExpenseCat createFromParcel(Parcel source) {
            return new ExpenseCat(source);
        }

        public ExpenseCat[] newArray(int size) {
            return new ExpenseCat[size];
        }
    };
}
