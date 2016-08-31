package com.silence.account.fragment;

import android.app.Activity;
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
import com.silence.account.adapter.IncomeSwipeAdapter;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.IncomeDao;
import com.silence.account.model.Income;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.ScreenUtils;
import com.silence.account.utils.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Silence on 2016/3/13 0013.
 */
public class ShowIncomeFgt extends ListFragment {
    @Bind(android.R.id.list)
    SwipeMenuListView mListFinance;
    private IncomeDao mIncomeDao;
    private List<Income> mIncomes;
    private IncomeSwipeAdapter mIncomeSwipeAdapter;
    private onIncomeChangeListener mOnIncomeChangeListener;
    private int mType;

    public static ShowIncomeFgt getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_DATE, type);
        ShowIncomeFgt showIncomeFgt = new ShowIncomeFgt();
        showIncomeFgt.setArguments(bundle);
        return showIncomeFgt;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onIncomeChangeListener) {
            mOnIncomeChangeListener = (onIncomeChangeListener) activity;
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
        mIncomeDao = new IncomeDao(getActivity());
        switch (mType) {
            case Constant.TYPE_MONTH:
                mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getMonthStart(),
                        DateUtils.getMonthEnd(), AccountApplication.sUser.getId());
                mIncomeSwipeAdapter = new IncomeSwipeAdapter(getActivity(), mIncomes, false);
                break;
            case Constant.TYPE_TODAY:
                mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getTodayStart(),
                        DateUtils.getTodayEnd(), AccountApplication.sUser.getId());
                mIncomeSwipeAdapter = new IncomeSwipeAdapter(getActivity(), mIncomes, true);
                break;
            case Constant.TYPE_WEEK:
                mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getWeekStart(),
                        DateUtils.getWeekEnd(), AccountApplication.sUser.getId());
                mIncomeSwipeAdapter = new IncomeSwipeAdapter(getActivity(), mIncomes, false);
                break;
            default:
                break;
        }
        setListAdapter(mIncomeSwipeAdapter);

        //创建菜单项，设置文字、图标、宽度、背景等属性
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
        //为菜单项设置点击监听事件
        mListFinance.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //如果删除成功，刷新数据，重绘界面
                if (mIncomeDao.deleteExpense((Income) mIncomeSwipeAdapter.getItem(mIncomes.size()
                        - 1 - position))) {
                    T.showShort(getActivity(), "删除成功");
                    mIncomes.remove(mIncomes.size() - 1 - position);
                    mIncomeSwipeAdapter.setData(mIncomes);
                    invalidateUI();
                    EventBus.getDefault().post("income_deleted");
                } else {
                    T.showShort(getActivity(), "删除失败");
                }
                return false;
            }
        });
        return view;
    }

    private void invalidateUI() {
        if (mOnIncomeChangeListener != null) {
            float income = 0;
            switch (mType) {
                case Constant.TYPE_MONTH:
                    income = mIncomeDao.getPeriodSumIncome(DateUtils.getMonthStart(),
                            DateUtils.getMonthEnd(), AccountApplication.sUser.getId());
                    break;
                case Constant.TYPE_TODAY:
                    income = mIncomeDao.getPeriodSumIncome(DateUtils.getTodayStart(),
                            DateUtils.getTodayEnd(), AccountApplication.sUser.getId());
                    break;
                case Constant.TYPE_WEEK:
                    income = mIncomeDao.getPeriodSumIncome(DateUtils.getWeekStart(),
                            DateUtils.getWeekEnd(), AccountApplication.sUser.getId());
                    break;
                default:
                    break;
            }
            mOnIncomeChangeListener.updateIncome(income);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUI(String message) {
        if (TextUtils.equals(message, "income_updated") && mOnIncomeChangeListener != null) {
            switch (mType) {
                case Constant.TYPE_MONTH:
                    mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getMonthStart(),
                            DateUtils.getMonthEnd(), AccountApplication.sUser.getId());
                    break;
                case Constant.TYPE_TODAY:
                    mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getTodayStart(),
                            DateUtils.getTodayEnd(), AccountApplication.sUser.getId());
                    break;
                case Constant.TYPE_WEEK:
                    mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getWeekStart(),
                            DateUtils.getWeekEnd(), AccountApplication.sUser.getId());
                    break;
                default:
                    break;
            }
            mIncomeSwipeAdapter.setData(mIncomes);
            invalidateUI();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnIncomeChangeListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Income income = (Income) l.getItemAtPosition(mIncomes.size() - 1 - position);
        Intent intent = new Intent(getActivity(), RecordActivity.class);
        intent.putExtra(Constant.RECORD, income);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface onIncomeChangeListener {
        void updateIncome(float income);
    }
}