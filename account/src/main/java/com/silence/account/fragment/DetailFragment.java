package com.silence.account.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.activity.ItemActivity;
import com.silence.account.activity.RecordActivity;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.Bind;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
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
        float incomes = mIncomeDao.getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
        mTextTodayIncome.setText(String.valueOf(incomes));
        float expenses = mExpenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
        mTextTodayExpense.setText(String.valueOf(expenses));
    }

    private void updateWeek() {
        Date start = DateUtils.getWeekStart();
        Date end = DateUtils.getWeekEnd();
        float incomes = mIncomeDao.getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
        mTextWeekIncome.setText(String.valueOf(incomes));
        float expenses = mExpenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
        mTextWeekExpense.setText(String.valueOf(expenses));
    }

    private void updateMonth() {
        Date start = DateUtils.getMonthStart();
        Date end = DateUtils.getMonthEnd();
        float incomes = mIncomeDao.getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
        mTextDetailIncome.setText(String.valueOf(incomes));
        mTextMonthIncome.setText(String.valueOf(incomes));
        float expenses = mExpenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
        mTextMonthExpense.setText(String.valueOf(expenses));
        mTextDetailExpense.setText(String.valueOf(expenses));
        float balance = incomes - expenses;
        mTextDetailBalance.setText(String.valueOf(balance));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.btn_record, R.id.ll_detail_today, R.id.ll_detail_week, R.id.ll_detail_month})
    public void detailClick(View view) {
        switch (view.getId()) {
            case R.id.ll_detail_today: {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(Constant.TYPE_DATE, Constant.TYPE_TODAY);
                startActivity(intent);
            }
            break;
            case R.id.ll_detail_week: {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(Constant.TYPE_DATE, Constant.TYPE_WEEK);
                startActivity(intent);
            }
            break;
            case R.id.ll_detail_month: {
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra(Constant.TYPE_DATE, Constant.TYPE_MONTH);
                startActivity(intent);
            }
            break;
            case R.id.btn_record: {
                startActivity(new Intent(getActivity(), RecordActivity.class));
            }
            break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUI(String message) {
        if (TextUtils.equals(message, "income_inserted") || TextUtils.equals(message, "expense_updated")
                || TextUtils.equals(message, "income_updated") || TextUtils.equals(message, "income_deleted")
                || TextUtils.equals(message, "expense_deleted") || TextUtils.equals(message, "expense_inserted")) {
            updateToday();
            updateWeek();
            updateMonth();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected Fragment getSubFragment() {
        return this;
    }
}
