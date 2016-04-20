package com.silence.account.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.silence.account.pojo.Income;
import com.silence.account.pojo.IncomeCat;
import com.silence.account.pojo.IncomeStatistics;
import com.silence.account.utils.DBOpenHelper;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.FormatUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Silence on 2016/3/11 0011.
 */
public class IncomeDao {
    private Dao<Income, Integer> mDao;
    private Context mContext;

    public IncomeDao(Context context) {
        mContext = context;
        mDao = DBOpenHelper.getInstance(context).getDao(Income.class);
    }

    public boolean addIncome(Income income) {
        int row = 0;
        try {
            row = mDao.create(income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean updateIncome(Income income) {
        int row = 0;
        try {
            row = mDao.update(income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public float getPeriodSumIncome(Date start, Date end) {
        List<Income> incomes = getPeriodIncomes(start, end);
        float sum = 0;
        if (incomes != null && incomes.size() > 0) {
            for (int i = 0; i < incomes.size(); i++) {
                sum += incomes.get(i).getAmount();
            }
        }
        return sum;
    }

    public List<IncomeStatistics> getPeriodCatSumExpense(Date start, Date end) {
        List<IncomeStatistics> incomeStatisticses = null;
        String sql = "select ASincomeCat.ASname, ASincomeCat.ASimage, sum(ASamount) sumCatIncome from ASincome " +
                ", ASincomeCat where ASincome.AScategoryId = ASincomeCat.ASid and ASdate between ? and ? and " +
                "AScategoryId in (select distinct(AScategoryId) from ASincome) group by AScategoryId;";
        SQLiteDatabase database = DBOpenHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, new String[]{DateUtils.date2Str(start), DateUtils.date2Str(end)});
        float sumExpense = getPeriodSumIncome(start, end);
        if (cursor.moveToFirst()) {
            incomeStatisticses = new ArrayList<>(cursor.getCount());
            do {
                String categoryName = cursor.getString(0);
                int imageId = cursor.getInt(1);
                float sumCat = cursor.getFloat(2);
                incomeStatisticses.add(new IncomeStatistics(new IncomeCat(imageId, categoryName),
                        sumCat, FormatUtils.formatFloat("#.0",sumCat / sumExpense * 100)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return incomeStatisticses;
    }

    public List<Income> getPeriodIncomes(Date start, Date end) {
        List<Income> incomes = null;
        try {
            incomes = mDao.queryBuilder().where().between("ASdate", start, end).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }

    public boolean deleteExpense(Income income) {
        int row = 0;
        try {
            row = mDao.delete(income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }
}
