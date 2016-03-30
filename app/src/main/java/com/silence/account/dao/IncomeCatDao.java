package com.silence.account.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.silence.account.R;
import com.silence.account.pojo.IncomeCat;
import com.silence.account.pojo.User;
import com.silence.account.utils.DBOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silence on 2016/3/10 0010.
 */
public class IncomeCatDao {
    private Dao<IncomeCat, Integer> mDao;

    public IncomeCatDao(Context context) {
        mDao = DBOpenHelper.getInstance(context).getDao(IncomeCat.class);
    }

    public List<IncomeCat> getIncomeCat(int userId) {
        List<IncomeCat> cats = null;
        try {
            cats = mDao.queryForEq("userId", userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cats;
    }

    public boolean addCategory(IncomeCat incomeCat) {
        int row = 0;
        try {
            row = mDao.create(incomeCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean update(IncomeCat incomeCat) {
        int row = 0;
        try {
            row = mDao.update(incomeCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean delete(IncomeCat incomeCat) {
        int row = 0;
        try {
            row = mDao.delete(incomeCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public void initIncomeCat(User user) {
        int resId[] = {R.mipmap.icon_shouru_type_gongzi, R.mipmap.icon_shouru_type_shenghuofei,
                R.mipmap.icon_shouru_type_hongbao, R.mipmap.icon_shouru_type_linghuaqian,
                R.mipmap.icon_shouru_type_jianzhiwaikuai, R.mipmap.icon_shouru_type_touzishouru,
                R.mipmap.icon_shouru_type_jieru, R.mipmap.icon_shouru_type_jiangjin, R.mipmap.huankuan,
                R.mipmap.baoxiao, R.mipmap.xianjin, R.mipmap.tuikuan, R.mipmap.zhifubao,
                R.mipmap.icon_shouru_type_qita};
        String labels[] = {"工资", "生活费", "红包", "零花钱", "兼职", "投资收益", "借入",
                "奖金", "还款", "报销", "现金", "退款", "支付宝", "其他"};
        List<IncomeCat> cats = new ArrayList<>(resId.length);
        IncomeCat incomeCat;
        for (int i = 0; i < resId.length; i++) {
            incomeCat = new IncomeCat();
            incomeCat.setUser(user);
            incomeCat.setImageId(resId[i]);
            incomeCat.setName(labels[i]);
            cats.add(incomeCat);
        }
        try {
            for (int i = 0, j = cats.size(); i < j; i++) {
                mDao.create(cats.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
