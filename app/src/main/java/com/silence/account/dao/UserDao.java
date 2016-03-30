package com.silence.account.dao;

import android.content.Context;

import com.silence.account.pojo.User;
import com.silence.account.utils.DBOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Silence on 2016/3/9 0009.
 */
public class UserDao {
    private DBOpenHelper mDBOpenHelper;
    private Dao<User, Integer> mDao;

    public UserDao(Context context) {
        mDBOpenHelper = DBOpenHelper.getInstance(context);
        mDao = DBOpenHelper.getInstance(context).getDao(User.class);
    }

    public User getCurrentUser(String username) {
        List<User> users = null;
        try {
            users = mDao.queryForEq("name", username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

    public void addUser(User user) {
//        SQLiteDatabase sqLiteDatabase = mDBOpenHelper.getWritableDatabase();
//        sqLiteDatabase.execSQL("insert into user (objectId, name, email) values (?,?,?);",
//                new Object[]{user.getObjectId(), user.getUsername(), user.getEmail()});
        try {
            mDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
