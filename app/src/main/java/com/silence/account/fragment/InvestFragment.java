package com.silence.account.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.activity.InvestActivity;
import com.silence.account.adapter.CommonAdapter;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.dao.InvestDao;
import com.silence.account.pojo.Invest;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Silence on 2016/3/7 0007.
 */
public class InvestFragment extends BaseFragment {

    @Bind(R.id.label_invest_remain)
    TextView mLabelInvestRemain;
    @Bind(R.id.list_invest)
    ListView mListInvest;
    @Bind(R.id.btn_invest)
    Button mBtnInvest;
    @Bind(R.id.label_invest_invest)
    TextView mLabelInvestInvest;
    @Bind(R.id.label_invest_idle)
    TextView mLabelInvestIdle;
    private Context mContext;
    private float mIdle;
    private static final int REQUEST_INVEST = 0x123;
    private InvestDao mInvestDao;
    private CommonAdapter mCommonAdapter;
    private float mRemain;
    private float mSumInvested;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invest, container, false);
        ButterKnife.bind(this, view);
        mInvestDao = new InvestDao(mContext);
        getRemain();
        List<Invest> invest = mInvestDao.findInvest(DateUtils.getMonthStart(), DateUtils.getMonthEnd(),AccountApplication.sUser.getId());
        mSumInvested = 0;
        for (int i = 0; i < invest.size(); i++) {
            mSumInvested += invest.get(i).getAmount();
        }
        mIdle = mRemain - mSumInvested;
        if (mIdle <= 0) {
            mBtnInvest.setEnabled(false);
        } else {
            mBtnInvest.setEnabled(true);
        }
        mLabelInvestIdle.setText(String.valueOf(mIdle));
        mLabelInvestInvest.setText(String.valueOf(mSumInvested));
        List<Invest> invests = mInvestDao.findAllInvest();
        mCommonAdapter = new CommonAdapter<Invest>(invests, R.layout.item_invest) {
            @Override
            public void bindView(ViewHolder holder, Invest obj) {
                holder.setText(R.id.label_item_invest_type, obj.getType());
                holder.setText(R.id.label_item_invest_amount, String.valueOf(obj.getAmount()));
                holder.setText(R.id.label_item_invest_year, String.valueOf(obj.getYear()));
                holder.setText(R.id.label_item_invest_rate, String.valueOf(obj.getRate()));
                holder.setText(R.id.label_item_invest_earning, String.valueOf(obj.getEarning()));
            }
        };
        mListInvest.setAdapter(mCommonAdapter);
        return view;
    }

    private void getRemain() {
        Date start = DateUtils.getMonthStart();
        Date end = DateUtils.getMonthEnd();
        float incomes = new IncomeDao(mContext).getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
        float expenses = new ExpenseDao(mContext).getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
        mRemain = incomes - expenses;
        mLabelInvestRemain.setText(String.valueOf(mRemain));
    }

    @Subscribe
    public void refreshAccount(String message) {
        if (TextUtils.equals(message, "income_inserted") || TextUtils.equals(message, "expense_updated")
                || TextUtils.equals(message, "income_updated") || TextUtils.equals(message, "income_deleted")
                || TextUtils.equals(message, "expense_deleted") || TextUtils.equals(message, "expense_inserted")) {
            getRemain();
            mIdle = mRemain - mSumInvested;
            if (mIdle <= 0) {
                mBtnInvest.setEnabled(false);
            } else {
                mBtnInvest.setEnabled(true);
            }
            mLabelInvestIdle.setText(String.valueOf(mIdle));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_INVEST && resultCode == Activity.RESULT_OK) {
            String stringExtra = data.getStringExtra(Constant.INVEST);
            mCommonAdapter.setData(mInvestDao.findAllInvest());
            mSumInvested += Float.parseFloat(stringExtra);
            mIdle = mRemain - mSumInvested;
            if (mIdle <= 0) {
                mBtnInvest.setEnabled(false);
            } else {
                mBtnInvest.setEnabled(true);
            }
            mLabelInvestInvest.setText(String.valueOf(mSumInvested));
            mLabelInvestIdle.setText(String.valueOf(mIdle));
        }
    }

    @OnClick(R.id.btn_invest)
    public void investClick() {
        Intent intent = new Intent(mContext, InvestActivity.class);
        intent.putExtra(Constant.INVEST, mIdle);
        startActivityForResult(intent, REQUEST_INVEST);
    }
}
