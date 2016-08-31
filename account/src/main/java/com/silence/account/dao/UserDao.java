package com.silence.account.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.silence.account.model.User;
import com.silence.account.utils.DBOpenHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Silence on 2016/3/9 0009.
 */
public class UserDao {
    private Dao<User, Integer> mDao;
    private final DBOpenHelper mDbOpenHelper;

    public UserDao(Context context) {
        mDbOpenHelper = DBOpenHelper.getInstance(context);
        mDao = mDbOpenHelper.getDao(User.class);
    }

    //获取当前用户
    public User getCurrentUser(String username) {
        List<User> users = null;
        try {
            users = mDao.queryForEq("ASname", username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

    //新用户登录
    public void addUser(User user) {
        try {
            mDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //修改用户名
    public void updatePass(String pass, int id) {
        SQLiteDatabase database = mDbOpenHelper.getWritableDatabase();
        String sql = "update ASuser set ASpassword=? where ASid=?;";
        database.execSQL(sql, new Object[]{pass, id});
    }

    //修改密码
    public void updateName(String name, int id) {
        SQLiteDatabase database = mDbOpenHelper.getWritableDatabase();
        String sql = "update ASuser set ASemail=? where ASid=?;";
        database.execSQL(sql, new Object[]{name, id});
    }

}
