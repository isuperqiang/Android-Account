package com.silence.account.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.fragment.ShowExpenseFgt;
import com.silence.account.fragment.ShowIncomeFgt;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class ItemActivity extends BaseActivity implements ShowExpenseFgt.onExpenseChangeListener,
        ShowIncomeFgt.onIncomeChangeListener {

    @Bind(R.id.label_show_remain)
    TextView mLabelShowRemain;
    @Bind(R.id.label_show_income)
    TextView mLabelShowIncome;
    @Bind(R.id.label_show_expense)
    TextView mLabelShowExpense;
    @Bind(R.id.btn_show_income)
    Button mBtnShowIncome;
    @Bind(R.id.btn_show_expense)
    Button mBtnShowExpense;
    private FragmentManager mFragmentManager;
    private ShowIncomeFgt mShowIncomeFgt;
    private ShowExpenseFgt mShowExpenseFgt;
    private float mIncome;
    private float mExpense;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        showBackwardView(true);
        mFragmentManager = getSupportFragmentManager();
        IncomeDao incomeDao = new IncomeDao(this);
        ExpenseDao expenseDao = new ExpenseDao(this);
        mType = getIntent().getIntExtra(Constant.TYPE_DATE, 0);
        switch (mType) {
            case Constant.TYPE_MONTH: {
                setTitle(getString(R.string.current_month));
                Date start = DateUtils.getMonthStart();
                Date end = DateUtils.getMonthEnd();
                mIncome = incomeDao.getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
                mExpense = expenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
                mLabelShowExpense.setText(String.valueOf(mExpense));
                mLabelShowIncome.setText(String.valueOf(mIncome));
                mLabelShowRemain.setText(String.valueOf(mIncome - mExpense));
                mShowExpenseFgt = ShowExpenseFgt.getInstance(Constant.TYPE_MONTH);
                mFragmentManager.beginTransaction().add(R.id.show_fragment, mShowExpenseFgt).commit();
            }
            break;
            case Constant.TYPE_TODAY: {
                setTitle(getString(R.string.today));
                Date start = DateUtils.getTodayStart();
                Date end = DateUtils.getTodayEnd();
                mIncome = incomeDao.getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
                mExpense = expenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
                mLabelShowExpense.setText(String.valueOf(mExpense));
                mLabelShowIncome.setText(String.valueOf(mIncome));
                mLabelShowRemain.setText(String.valueOf(mIncome - mExpense));
                mShowExpenseFgt = ShowExpenseFgt.getInstance(Constant.TYPE_TODAY);
                mFragmentManager.beginTransaction().add(R.id.show_fragment, mShowExpenseFgt).commit();
            }
            break;
            case Constant.TYPE_WEEK: {
                setTitle(getString(R.string.current_week));
                Date start = DateUtils.getWeekStart();
                Date end = DateUtils.getWeekEnd();
                mIncome = incomeDao.getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
                mExpense = expenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
                mLabelShowExpense.setText(String.valueOf(mExpense));
                mLabelShowIncome.setText(String.valueOf(mIncome));
                mLabelShowRemain.setText(String.valueOf(mIncome - mExpense));
                mShowExpenseFgt = ShowExpenseFgt.getInstance(Constant.TYPE_WEEK);
                mFragmentManager.beginTransaction().add(R.id.show_fragment, mShowExpenseFgt).commit();
            }
            break;
        }
    }

    @Override
    protected Activity getSubActivity() {
        return this;
    }

    private void hideAll(FragmentTransaction transaction) {
        if (mShowIncomeFgt != null && mShowIncomeFgt.isVisible()) {
            transaction.hide(mShowIncomeFgt);
        }
        if (mShowExpenseFgt != null && mShowExpenseFgt.isVisible()) {
            transaction.hide(mShowExpenseFgt);
        }
    }

    @OnClick({R.id.btn_show_income, R.id.btn_show_expense})
    public void itemClick(View view) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAll(transaction);
        switch (view.getId()) {
            case R.id.btn_show_income:
                mBtnShowIncome.setBackgroundResource(R.drawable.btn_orange_pressed);
                mBtnShowExpense.setBackgroundResource(R.drawable.btn_orange_normal);
                if (mShowIncomeFgt == null) {
                    mShowIncomeFgt = ShowIncomeFgt.getInstance(mType);
                    transaction.add(R.id.show_fragment, mShowIncomeFgt);
                } else {
                    transaction.show(mShowIncomeFgt);
                }
                break;
            case R.id.btn_show_expense:
                mBtnShowIncome.setBackgroundResource(R.drawable.btn_orange_normal);
                mBtnShowExpense.setBackgroundResource(R.drawable.btn_orange_pressed);
                if (mShowExpenseFgt == null) {
                    mShowExpenseFgt = ShowExpenseFgt.getInstance(mType);
                    transaction.add(R.id.show_fragment, mShowExpenseFgt);
                } else {
                    transaction.show(mShowExpenseFgt);
                }
                break;
        }
        transaction.commit();
    }


    @Override
    public void updateExpense(float expense) {
        mLabelShowExpense.setText(String.valueOf(expense));
        mExpense = expense;
        mLabelShowRemain.setText(String.valueOf(mIncome - expense));
        EventBus.getDefault().post("updated");
    }

    @Override
    public void updateIncome(float income) {
        EventBus.getDefault().post("updated");
        mLabelShowIncome.setText(String.valueOf(income));
        mIncome = income;
        mLabelShowRemain.setText(String.valueOf(income - mExpense));
    }
}