package com.silence.account.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.pojo.AllYearStatistics;

import java.util.List;

/**
 * Created by Silence on 2016/4/8 0008.
 */
public class AllYearStatisticAdapter extends BaseAdapter {
    private Context mContext;
    private List<AllYearStatistics> mAllYearStatisticses;

    public AllYearStatisticAdapter(Context context, List<AllYearStatistics> allYearStatisticses) {
        mContext = context;
        mAllYearStatisticses = allYearStatisticses;
    }

    @Override
    public int getCount() {
        return mAllYearStatisticses == null ? 0 : mAllYearStatisticses.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllYearStatisticses == null ? null : mAllYearStatisticses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_line_chart, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mTextMonth = (TextView) convertView.findViewById(R.id.label_item_line_month);
            viewHolder.mTextIncome = (TextView) convertView.findViewById(R.id.label_item_line_income);
            viewHolder.mTextExpense = (TextView) convertView.findViewById(R.id.label_item_line_expense);
            viewHolder.mTextBalance = (TextView) convertView.findViewById(R.id.label_item_line_balance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AllYearStatistics allYearStatistics = mAllYearStatisticses.get(mAllYearStatisticses.size()-1 - position);
        viewHolder.mTextMonth.setText(allYearStatistics.getMonth() + "月");
        viewHolder.mTextIncome.setText(String.valueOf(allYearStatistics.getIncome()));
        viewHolder.mTextExpense.setText(String.valueOf(allYearStatistics.getExpense()));
        viewHolder.mTextBalance.setText(String.valueOf(allYearStatistics.getBalance()));
        if (allYearStatistics.getBalance() < 0) {
            viewHolder.mTextBalance.setTextColor(Color.RED);
        } else {
            viewHolder.mTextBalance.setTextColor(mContext.getResources().getColor(R.color.color_text_c7));
        }
        return convertView;
    }

    public void setData(List<AllYearStatistics> allYearStatisticses) {
        mAllYearStatisticses = allYearStatisticses;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView mTextMonth;
        TextView mTextIncome;
        TextView mTextExpense;
        TextView mTextBalance;
    }
}
