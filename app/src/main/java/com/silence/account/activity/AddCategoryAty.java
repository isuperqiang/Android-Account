package com.silence.account.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.silence.account.R;
import com.silence.account.adapter.CommonAdapter;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.ExpenseCatDao;
import com.silence.account.dao.IncomeCatDao;
import com.silence.account.pojo.ExpenseCat;
import com.silence.account.pojo.IncomeCat;
import com.silence.account.utils.Constant;
import com.silence.account.utils.L;
import com.silence.account.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Silence on 2016/3/10 0010.
 */
public class AddCategoryAty extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.icon_add_category)
    ImageView mIconAddCategory;
    @Bind(R.id.et_add_category)
    EditText mEtAddCategory;
    @Bind(R.id.grid_add_category)
    GridView mGridAddCategory;
    private int mResId;
    private ExpenseCat mExpenseCat;
    private IncomeCat mIncomeCat;
    private String mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        setTitle("添加类别");
        showBackwardView(true);
        showForwardView(true);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        Parcelable extra = getIntent().getParcelableExtra(Constant.UPDATE_CAT);
        if (extra != null) {
            setTitle("修改类别");
            if (extra instanceof ExpenseCat) {
                mType = Constant.TYPE_EXPENSE;
                mExpenseCat = (ExpenseCat) extra;
                mEtAddCategory.setText(mExpenseCat.getName());
                mIconAddCategory.setImageResource(mExpenseCat.getImageId());
                mResId = mExpenseCat.getImageId();
            } else {
                mType = Constant.TYPE_INCOME;
                mIncomeCat = (IncomeCat) extra;
                mEtAddCategory.setText(mIncomeCat.getName());
                mIconAddCategory.setImageResource(mIncomeCat.getImageId());
                mResId = mIncomeCat.getImageId();
            }
        } else {
            mResId = R.mipmap.icon_shouru_type_qita;
            mType = getIntent().getStringExtra(Constant.TYPE_CATEGORY);
            setTitle("添加类别");
        }
        CommonAdapter commonAdapter = new CommonAdapter<Integer>(initData(), R.layout.item_add_category) {
            @Override
            public void bindView(ViewHolder holder, Integer obj) {
                holder.setImageResource(R.id.item_add_category, obj);
            }
        };
        mGridAddCategory.setAdapter(commonAdapter);
        mGridAddCategory.setOnItemClickListener(this);
    }

    private List<Integer> initData() {
        int resId[] = {R.mipmap.icon_zhichu_type_canyin, R.mipmap.maicai, R.mipmap.icon_zhichu_type_yanjiuyinliao,
                R.mipmap.icon_zhichu_type_shuiguolingshi, R.mipmap.baojian, R.mipmap.ad, R.mipmap.anjie,
                R.mipmap.baobao, R.mipmap.baoxian, R.mipmap.baoxiao, R.mipmap.chuanpiao, R.mipmap.daoyou,
                R.mipmap.dapai, R.mipmap.dianfei, R.mipmap.dianying, R.mipmap.fangdai, R.mipmap.fangzu,
                R.mipmap.fanka, R.mipmap.feijipiao, R.mipmap.fuwu, R.mipmap.gonggongqiche, R.mipmap.haiwaidaigou,
                R.mipmap.huankuan, R.mipmap.huazhuangpin, R.mipmap.huochepiao, R.mipmap.huwaishebei,
                R.mipmap.icon_add_1, R.mipmap.icon_add_2, R.mipmap.icon_add_3, R.mipmap.icon_add_4,
                R.mipmap.icon_add_5, R.mipmap.icon_add_6, R.mipmap.icon_add_7, R.mipmap.icon_add_8,
                R.mipmap.icon_add_9, R.mipmap.icon_add_10, R.mipmap.icon_add_11, R.mipmap.icon_add_12,
                R.mipmap.icon_add_13, R.mipmap.icon_add_14, R.mipmap.icon_add_15, R.mipmap.icon_add_16,
                R.mipmap.icon_add_17, R.mipmap.icon_add_18, R.mipmap.icon_add_19, R.mipmap.icon_add_20,
                R.mipmap.icon_shouru_type_gongzi, R.mipmap.icon_shouru_type_hongbao, R.mipmap.icon_shouru_type_jiangjin,
                R.mipmap.icon_shouru_type_jianzhiwaikuai, R.mipmap.icon_shouru_type_jieru,
                R.mipmap.icon_shouru_type_linghuaqian, R.mipmap.icon_shouru_type_shenghuofei,
                R.mipmap.icon_shouru_type_touzishouru, R.mipmap.icon_zhichu_type_baoxiaozhang, R.mipmap.icon_zhichu_type_canyin,
                R.mipmap.icon_zhichu_type_chongwu, R.mipmap.icon_zhichu_type_gouwu, R.mipmap.icon_zhichu_type_jiaotong,
                R.mipmap.icon_zhichu_type_jiechu, R.mipmap.icon_zhichu_type_jujia, R.mipmap.icon_zhichu_type_meirongjianshen,
                R.mipmap.icon_zhichu_type_renqingsongli, R.mipmap.icon_zhichu_type_shoujitongxun,
                R.mipmap.icon_zhichu_type_shuji, R.mipmap.icon_zhichu_type_taobao, R.mipmap.icon_zhichu_type_yanjiuyinliao,
                R.mipmap.icon_zhichu_type_yiban, R.mipmap.icon_zhichu_type_yule, R.mipmap.jiushui,
                R.mipmap.juechu, R.mipmap.kuzi, R.mipmap.lifa, R.mipmap.lingqian, R.mipmap.lingshi,
                R.mipmap.lvyoudujia, R.mipmap.majiang, R.mipmap.mao, R.mipmap.naifen, R.mipmap.party,
                R.mipmap.quxian, R.mipmap.richangyongpin, R.mipmap.shuifei, R.mipmap.shumachanpin,
                R.mipmap.sijiache, R.mipmap.tingchefei, R.mipmap.tuikuan, R.mipmap.wanfan, R.mipmap.wangfei,
                R.mipmap.wanggou, R.mipmap.wanju, R.mipmap.weixiubaoyang, R.mipmap.wuye, R.mipmap.xianjin,
                R.mipmap.xiaochi, R.mipmap.xiaojingjiazhang, R.mipmap.xiezi, R.mipmap.xinyongkahuankuan,
                R.mipmap.xizao, R.mipmap.xuefei, R.mipmap.yan, R.mipmap.yaopinfei, R.mipmap.yifu, R.mipmap.yinhangshouxufei,
                R.mipmap.yiwaiposun, R.mipmap.yiwaisuode, R.mipmap.youfei, R.mipmap.youxi, R.mipmap.yuegenghuan,
                R.mipmap.yundongjianshen, R.mipmap.zhifubao, R.mipmap.zhongfan, R.mipmap.zhuanzhang,
                R.mipmap.zhusu, R.mipmap.zuojifei};
        List<Integer> resIds = new ArrayList<>(resId.length);
        for (int aResId : resId) {
            resIds.add(aResId);
        }
        return resIds;
    }

    @Override
    protected void onForward() {
        String name = mEtAddCategory.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            if (TextUtils.equals(mType, Constant.TYPE_INCOME)) {
                IncomeCatDao incomeCatDao = new IncomeCatDao(this);
                if (mIncomeCat != null) {
                    if (incomeCatDao.update(new IncomeCat(mIncomeCat.getId(), name, mResId, AccountApplication.sUser))) {
                        T.showShort(this, "修改成功");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        T.showShort(this, "修改失败");
                    }
                } else {
                    if (incomeCatDao.addCategory(new IncomeCat(mResId, name, AccountApplication.sUser))) {
                        T.showShort(this, "保存成功");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        T.showShort(this, "保存失败");
                    }
                }
            } else if (TextUtils.equals(mType, Constant.TYPE_EXPENSE)) {
                ExpenseCatDao expenseCatDao = new ExpenseCatDao(this);
                L.i("expense cat");
                if (mExpenseCat != null) {
                    if (expenseCatDao.update(new ExpenseCat(mExpenseCat.getId(), name, mResId, AccountApplication.sUser))) {
                        T.showShort(this, "修改成功");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        T.showShort(this, "修改失败");
                    }
                } else {
                    if (expenseCatDao.addCategory(new ExpenseCat(mResId, name, AccountApplication.sUser))) {
                        T.showShort(this, "保存成功");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        T.showShort(this, "保存失败");
                    }
                }
            }
        } else {
            T.showShort(this, "请填写类别名称");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mResId = (int) parent.getItemAtPosition(position);
        mIconAddCategory.setImageResource(mResId);
    }
}