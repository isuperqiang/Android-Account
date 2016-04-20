package com.silence.account.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.silence.account.pojo.Invest;
import com.silence.account.utils.DBOpenHelper;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Silence on 2016/4/13 0013.
 */
public class InvestDao {
    private Dao<Invest, Integer> mDao;

    public InvestDao(Context context) {
        mDao = DBOpenHelper.getInstance(context).getDao(Invest.class);
    }

    public boolean addInvest(Invest invest) {
        int row = 0;
        try {
            row = mDao.create(invest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean deleteInvest(Invest invest) {
        int row = 0;
        try {
            row = mDao.delete(invest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean updateInvest(Invest invest) {
        int row = 0;
        try {
            row = mDao.update(invest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public List<Invest> findAllInvest() {
        List<Invest> invests = null;
        try {
            invests = mDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invests;
    }

    public List<Invest> findInvest(Date start, Date end) {
        List<Invest> invests = null;
        try {
            invests = mDao.queryBuilder().where().between("ASdate", start, end).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invests;
    }
}
