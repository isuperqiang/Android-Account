package com.silence.account.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.silence.account.R;
import com.silence.account.activity.RecordActivity;
import com.silence.account.adapter.ExpenseSwipeAdapter;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.pojo.Expense;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.ScreenUtils;
import com.silence.account.utils.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Silence on 2016/3/13 0013.
 */
public class ShowExpenseFgt extends ListFragment {
    @Bind(android.R.id.list)
    SwipeMenuListView mListFinance;
    private ExpenseDao mExpenseDao;
    private ExpenseSwipeAdapter mExpenseSwipeAdapter;
    private onExpenseChangeListener mOnExpenseChangeListener;
    private List<Expense> mExpenses;
    private int mType;

    public static ShowExpenseFgt getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_DATE, type);
        ShowExpenseFgt showExpenseFgt = new ShowExpenseFgt();
        showExpenseFgt.setArguments(bundle);
        return showExpenseFgt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onExpenseChangeListener) {
            mOnExpenseChangeListener = (onExpenseChangeListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(Constant.TYPE_DATE);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);
        ButterKnife.bind(this, view);
        mExpenseDao = new ExpenseDao(getActivity());
        switch (mType) {
            case Constant.TYPE_MONTH:
                mExpenses = mExpenseDao.getPeriodExpense(DateUtils.getMonthStart(), DateUtils.getMonthEnd());
                mExpenseSwipeAdapter = new ExpenseSwipeAdapter(getActivity(), mExpenses, false);
                break;
            case Constant.TYPE_TODAY:
                mExpenses = mExpenseDao.getPeriodExpense(DateUtils.getTodayStart(), DateUtils.getTodayEnd());
                mExpenseSwipeAdapter = new ExpenseSwipeAdapter(getActivity(), mExpenses, true);
                break;
            case Constant.TYPE_WEEK:
                mExpenses = mExpenseDao.getPeriodExpense(DateUtils.getWeekStart(), DateUtils.getWeekEnd());
                mExpenseSwipeAdapter = new ExpenseSwipeAdapter(getActivity(), mExpenses, false);
                break;
            default:
                break;
        }
        setListAdapter(mExpenseSwipeAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(ScreenUtils.dp2px(getActivity(), 80));
                deleteItem.setIcon(R.drawable.btn_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListFinance.setMenuCreator(creator);
        mListFinance.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (mExpenseDao.deleteExpense((Expense) mExpenseSwipeAdapter.getItem(mExpenses.size() - 1 - position))) {
                    T.showShort(getActivity(), "删除成功");
                    mExpenses.remove(mExpenses.size() - 1 - position);
                    mExpenseSwipeAdapter.setData(mExpenses);
                    EventBus.getDefault().post("expense_deleted");
                    invalidateUI();
                } else {
                    T.showShort(getActivity(), "删除失败");
                }
                return false;
            }
        });
        return view;
    }

    private void invalidateUI() {
        if (mOnExpenseChangeListener != null) {
            float expense = 0;
            switch (mType) {
                case Constant.TYPE_MONTH:
                    expense = mExpenseDao.getPeriodSumExpense(DateUtils.getMonthStart(), DateUtils.getMonthEnd());
                    break;
                case Constant.TYPE_TODAY:
                    expense = mExpenseDao.getPeriodSumExpense(DateUtils.getTodayStart(), DateUtils.getTodayEnd());
                    break;
                case Constant.TYPE_WEEK:
                    expense = mExpenseDao.getPeriodSumExpense(DateUtils.getWeekStart(), DateUtils.getWeekEnd());
                    break;
                default:
                    break;
            }
            mOnExpenseChangeListener.updateExpense(expense);
        }
    }

    @Subscribe
    public void refreshUI(String message) {
        if (TextUtils.equals(message, "expense_updated") && mExpenseSwipeAdapter != null) {
            switch (mType) {
                case Constant.TYPE_MONTH:
                    mExpenses = mExpenseDao.getPeriodExpense(DateUtils.getMonthStart(), DateUtils.getMonthEnd());
                    break;
                case Constant.TYPE_TODAY:
                    mExpenses = mExpenseDao.getPeriodExpense(DateUtils.getTodayStart(), DateUtils.getTodayEnd());
                    break;
                case Constant.TYPE_WEEK:
                    mExpenses = mExpenseDao.getPeriodExpense(DateUtils.getWeekStart(), DateUtils.getWeekEnd());
                    break;
                default:
                    break;
            }
            mExpenseSwipeAdapter.setData(mExpenses);
            invalidateUI();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnExpenseChangeListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Expense expense = (Expense) l.getItemAtPosition(mExpenses.size() - 1 - position);
        Intent intent = new Intent(getActivity(), RecordActivity.class);
        intent.putExtra(Constant.RECORD, expense);
        startActivityForResult(intent, Constant.REQUEST_UPDATE_FINANCE);
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

    public interface onExpenseChangeListener {
        void updateExpense(float expense);
    }
}
