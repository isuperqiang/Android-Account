package com.silence.account.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.silence.account.R;
import com.silence.account.activity.RecordActivity;
import com.silence.account.adapter.IncomeSwipeAdapter;
import com.silence.account.dao.IncomeDao;
import com.silence.account.pojo.Income;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.ScreenUtils;
import com.silence.account.utils.T;

import java.util.List;

/**
 * Created by Silence on 2016/3/13 0013.
 */
public class ShowIncomeFgt extends BaseFragment implements AdapterView.OnItemClickListener {
    private IncomeDao mIncomeDao;
    private boolean mIsUpdate;
    private List<Income> mIncomes;
    private IncomeSwipeAdapter mIncomeSwipeAdapter;
    private onIncomeChangeListener mOnIncomeChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onIncomeChangeListener) {
            mOnIncomeChangeListener = (onIncomeChangeListener) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsUpdate && mIncomeSwipeAdapter != null) {
            mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getTodayStart(), DateUtils.getTodayEnd());
            mIncomeSwipeAdapter.setData(mIncomes);
            if (mOnIncomeChangeListener != null) {
                invalidateUI();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mIncomeDao = new IncomeDao(getActivity());
        mIncomes = mIncomeDao.getPeriodIncomes(DateUtils.getTodayStart(), DateUtils.getTodayEnd());
        mIncomeSwipeAdapter = new IncomeSwipeAdapter(getActivity(), mIncomes);
        View view = inflater.inflate(R.layout.fragment_show_finance, container, false);
        SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.list_finance);
        listView.setAdapter(mIncomeSwipeAdapter);
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
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (mIncomeDao.deleteExpense((Income) mIncomeSwipeAdapter.getItem(position))) {
                    T.showShort(getActivity(), "删除成功");
                    mIncomes.remove(position);
                    mIncomeSwipeAdapter.notifyDataSetChanged();
                    invalidateUI();
                } else {
                    T.showShort(getActivity(), "删除失败");
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        listView.setOnItemClickListener(this);
        return view;
    }

    private void invalidateUI() {
        if (mOnIncomeChangeListener != null) {
            mOnIncomeChangeListener.updateIncome(mIncomeDao.getPeriodSumIncome(DateUtils.getTodayStart(),
                    DateUtils.getTodayEnd()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mIsUpdate = requestCode == Constant.REQUEST_UPDATE_FINANCE && resultCode == Constant.RESULT_UPDATE_FINANCE;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnIncomeChangeListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Income income = (Income) parent.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), RecordActivity.class);
        intent.putExtra(Constant.RECORD, income);
        startActivityForResult(intent, Constant.REQUEST_UPDATE_FINANCE);
    }

    public interface onIncomeChangeListener {
        void updateIncome(float income);
    }
}
