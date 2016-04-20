package com.silence.account.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.bmob.v3.BmobUser;

/**
 * Created by Silence on 2016/3/6 0006.
 * 用户实体类
 */
@DatabaseTable(tableName = "ASuser")
public class User extends BmobUser implements Parcelable {
    @DatabaseField(generatedId = true, columnName = "ASid")
    private int mId;
    @DatabaseField(columnName = "ASname")  //昵称
    private String mName;
    @DatabaseField(columnName = "ASphoto")
    private String mPicture;      //头像

    public User() {
    }

    public User(String name, String picture) {
        mName = name;
        mPicture = picture;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mPicture);
    }

    protected User(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mPicture = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
