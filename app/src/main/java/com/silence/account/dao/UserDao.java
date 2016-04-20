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
    private Dao<User, Integer> mDao;

    public UserDao(Context context) {
        mDao = DBOpenHelper.getInstance(context).getDao(User.class);
    }

    public User getCurrentUser(String username) {
        List<User> users = null;
        try {
            users = mDao.queryForEq("ASname", username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

    public void addUser(User user) {
        try {
            mDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
