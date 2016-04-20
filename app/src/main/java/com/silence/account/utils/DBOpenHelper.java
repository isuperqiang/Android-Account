package com.silence.account.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.silence.account.pojo.Expense;
import com.silence.account.pojo.ExpenseCat;
import com.silence.account.pojo.Income;
import com.silence.account.pojo.IncomeCat;
import com.silence.account.pojo.Invest;
import com.silence.account.pojo.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Silence on 2016/3/9 0009.
 */
public class DBOpenHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "ASaccount.db";
    private static final int DB_VERSION = 1;
    private static DBOpenHelper sDBOpenHelper;
    private Map<String, Dao> mDaoMap;

    public static DBOpenHelper getInstance(Context context) {
        if (sDBOpenHelper == null) {
            synchronized (DBOpenHelper.class) {
                if (sDBOpenHelper == null) {
                    sDBOpenHelper = new DBOpenHelper(context.getApplicationContext());
                }
            }
        }
        return sDBOpenHelper;
    }

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDaoMap = new HashMap<>();
    }

    public void dropTable() {
        SQLiteDatabase database = sDBOpenHelper.getWritableDatabase();
        database.execSQL("delete from ASuser;");
        database.execSQL("delete from ASincomeCat;");
        database.execSQL("delete from ASexpenseCat;");
        database.execSQL("delete from ASincome;");
        database.execSQL("delete from ASexpense;");
        database.execSQL("delete from ASinvest;");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, IncomeCat.class);
            TableUtils.createTable(connectionSource, ExpenseCat.class);
            TableUtils.createTable(connectionSource, Income.class);
            TableUtils.createTable(connectionSource, Expense.class);
            TableUtils.createTable(connectionSource, Invest.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
    }

    @Override
    public synchronized Dao getDao(Class clazz) {
        String className = clazz.getSimpleName();
        Dao dao = null;
        if (mDaoMap.containsKey(className)) {
            dao = mDaoMap.get(className);
        } else {
            try {
                dao = super.getDao(clazz);
                mDaoMap.put(className, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        mDaoMap.clear();
    }
}
