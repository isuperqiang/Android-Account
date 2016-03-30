package com.silence.account.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.fragment.ShowExpenseFgt;
import com.silence.account.fragment.ShowIncomeFgt;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    private boolean mIsUpdate;

    @Override
    public void initView() {
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);
        disPlayBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        IncomeDao incomeDao = new IncomeDao(this);
        ExpenseDao expenseDao = new ExpenseDao(this);
        int type = getIntent().getIntExtra(Constant.TYPE_DATE, 0);
        switch (type) {
            case Constant.TYPE_MONTH: {
                setActionTitle("本月");
                Date start = DateUtils.getMonthStart();
                Date end = DateUtils.getMonthEnd();
                mIncome = incomeDao.getPeriodSumIncome(start, end);
                mExpense = expenseDao.getPeriodSumExpense(start, end);
                mLabelShowExpense.setText(String.valueOf(mExpense));
                mLabelShowIncome.setText(String.valueOf(mIncome));
                mLabelShowRemain.setText(String.valueOf(mIncome - mExpense));
            }
            break;
            case Constant.TYPE_TODAY: {
                setActionTitle("今天");
                Date start = DateUtils.getTodayStart();
                Date end = DateUtils.getTodayEnd();
                mIncome = incomeDao.getPeriodSumIncome(start, end);
                mExpense = expenseDao.getPeriodSumExpense(start, end);
                mLabelShowExpense.setText(String.valueOf(mExpense));
                mLabelShowIncome.setText(String.valueOf(mIncome));
                mLabelShowRemain.setText(String.valueOf(mIncome - mExpense));
                mShowExpenseFgt = new ShowExpenseFgt();
                mFragmentManager.beginTransaction().add(R.id.show_fragment, mShowExpenseFgt).commit();
            }
            break;
            case Constant.TYPE_WEEK: {
                setActionTitle("本周");
                Date start = DateUtils.getWeekStart();
                Date end = DateUtils.getWeekEnd();
                mIncome = incomeDao.getPeriodSumIncome(start, end);
                mExpense = expenseDao.getPeriodSumExpense(start, end);
                mLabelShowExpense.setText(String.valueOf(mExpense));
                mLabelShowIncome.setText(String.valueOf(mIncome));
                mLabelShowRemain.setText(String.valueOf(mIncome - mExpense));
            }
            break;
        }
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
    public void onClick(View view) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAll(transaction);
        switch (view.getId()) {
            case R.id.btn_show_income:
                mBtnShowIncome.setBackgroundResource(R.drawable.btn_orange_pressed);
                mBtnShowExpense.setBackgroundResource(R.drawable.btn_orange_normal);
                if (mShowIncomeFgt == null) {
                    mShowIncomeFgt = new ShowIncomeFgt();
                    transaction.add(R.id.show_fragment, mShowIncomeFgt);
                } else {
                    transaction.show(mShowIncomeFgt);
                }
                break;
            case R.id.btn_show_expense:
                mBtnShowIncome.setBackgroundResource(R.drawable.btn_orange_normal);
                mBtnShowExpense.setBackgroundResource(R.drawable.btn_orange_pressed);
                if (mShowExpenseFgt == null) {
                    mShowExpenseFgt = new ShowExpenseFgt();
                    transaction.add(R.id.show_fragment, mShowExpenseFgt);
                } else {
                    transaction.show(mShowExpenseFgt);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mIsUpdate) {
                setResult(Constant.RESULT_REFRESH_FINANCE);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void updateExpense(float expense) {
        mLabelShowExpense.setText(String.valueOf(expense));
        mLabelShowRemain.setText(String.valueOf(mIncome - expense));
        mIsUpdate = true;
    }

    @Override
    public void updateIncome(float income) {
        mLabelShowIncome.setText(String.valueOf(income));
        mLabelShowRemain.setText(String.valueOf(income - mExpense));
        mIsUpdate = true;
    }
}