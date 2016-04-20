package com.silence.account.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Silence on 2016/3/7 0007.
 * 收入分类表
 */
@DatabaseTable(tableName = "ASincomeCat")
public class IncomeCat implements Parcelable {
    @DatabaseField(generatedId = true, columnName = "ASid")
    private int mId;        //主键
    @DatabaseField(canBeNull = false, columnName = "ASname")
    private String mName;   //名称
    @DatabaseField(canBeNull = false, columnName = "ASimage")
    private int mImageId;   //图标
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "ASuserId")
    private User mUser;     //用户

    public IncomeCat() {
    }

    public IncomeCat(int id, String name, int imageId, User user) {
        mId = id;
        mName = name;
        mImageId = imageId;
        mUser = user;
    }

    public IncomeCat(int imageId, String name, User user) {
        mImageId = imageId;
        mName = name;
        mUser = user;
    }

    public IncomeCat(int imageId,String name) {
        mName = name;
        mImageId = imageId;
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
        dest.writeParcelable(this.mUser, 0);
    }

    protected IncomeCat(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mImageId = in.readInt();
        this.mUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<IncomeCat> CREATOR = new Parcelable.Creator<IncomeCat>() {
        public IncomeCat createFromParcel(Parcel source) {
            return new IncomeCat(source);
        }

        public IncomeCat[] newArray(int size) {
            return new IncomeCat[size];
        }
    };
}
