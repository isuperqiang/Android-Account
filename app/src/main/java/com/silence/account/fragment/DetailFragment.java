package com.silence.account.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.activity.ItemActivity;
import com.silence.account.activity.RecordActivity;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Silence on 2016/3/7 0007.
 */
public class DetailFragment extends BaseFragment {

    @Bind(R.id.text_detail_income)
    TextView mTextDetailIncome;
    @Bind(R.id.text_detail_expense)
    TextView mTextDetailExpense;
    @Bind(R.id.text_detail_balance)
    TextView mTextDetailBalance;
    @Bind(R.id.text_today_income)
    TextView mTextTodayIncome;
    @Bind(R.id.text_today_expense)
    TextView mTextTodayExpense;
    @Bind(R.id.text_week_income)
    TextView mTextWeekIncome;
    @Bind(R.id.text_week_expense)
    TextView mTextWeekExpense;
    @Bind(R.id.text_month_income)
    TextView mTextMonthIncome;
    @Bind(R.id.text_month_expense)
    TextView mTextMonthExpense;
    private IncomeDao mIncomeDao;
    private ExpenseDao mExpenseDao;
    private static final int REQUEST_UPDATE = 0X1;
    private boolean mIsUpdate;

    @Override
    public void onStart() {
        super.onStart();
        if (mIsUpdate) {
            updateToday();
            updateWeek();
            updateMonth();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        mIncomeDao = new IncomeDao(getActivity());
        mExpenseDao = new ExpenseDao(getActivity());
        updateToday();
        updateWeek();
        updateMonth();
        return view;
    }

    private void updateToday() {
        Date start = DateUtils.getTodayStart();
        Date end = DateUtils.getTodayEnd();
        float incomes = mIncomeDao.getPeriodSumIncome(start, end);
        mTextTodayIncome.setText(String.valueOf(incomes));
        float expenses = mExpenseDao.getPeriodSumExpense(start, end);
        mTextTodayExpense.setText(String.valueOf(expenses));
    }

    private void updateWeek() {
        Date start = DateUtils.getWeekStart();
        Date end = DateUtils.getWeekEnd();
        float incomes = mIncomeDao.getPeriodSumIncome(start, end);
        mTextWeekIncome.setText(String.valueOf(incomes));
        float expenses = mExpenseDao.getPeriodSumExpense(start, end);
        mTextWeekExpense.setText(String.valueOf(expenses));
    }

    private void updateMonth() {
        Date start = DateUtils.getMonthStart();
        Date end = DateUtils.getMonthEnd();
        float incomes = mIncomeDao.getPeriodSumIncome(start, end);
        mTextDetailIncome.setText(String.valueOf(incomes));
        mTextMonthIncome.setText(String.valueOf(incomes));
        float expenses = mExpenseDao.getPeriodSumExpense(start, end);
        mTextMonthExpense.setText(String.valueOf(expenses));
        mTextDetailExpense.setText(String.valueOf(expenses));
        float balance = incomes - expenses;
        mTextDetailBalance.setText(String.valueOf(balance));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_record, R.id.ll_detail_today, R.id.ll_detail_week, R.id.ll_detail_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_detail_today: {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(Constant.TYPE_DATE, Constant.TYPE_TODAY);
                startActivityForResult(intent, REQUEST_UPDATE);
            }
            break;
            case R.id.ll_detail_week: {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(Constant.TYPE_DATE, Constant.TYPE_WEEK);
                startActivityForResult(intent, REQUEST_UPDATE);
            }
            break;
            case R.id.ll_detail_month: {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(Constant.TYPE_DATE, Constant.TYPE_MONTH);
                startActivityForResult(intent, REQUEST_UPDATE);
            }
            break;
            case R.id.btn_record: {
                startActivityForResult(new Intent(getActivity(), RecordActivity.class), REQUEST_UPDATE);
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATE) {
            if (resultCode == Constant.RESULT_INSERT_FINANCE || resultCode == Constant.RESULT_REFRESH_FINANCE) {
                mIsUpdate = true;
            } else {
                mIsUpdate = false;
            }
        }
    }
}
