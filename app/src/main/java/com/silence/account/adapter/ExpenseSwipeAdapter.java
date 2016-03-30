package com.silence.account.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipeListAdapter;
import com.silence.account.R;
import com.silence.account.pojo.Expense;

import java.util.List;

/**
 * Created by Silence on 2016/3/14 0014.
 */
public class ExpenseSwipeAdapter extends BaseSwipeListAdapter {
    private Context mContext;
    private List<Expense> mExpenses;

    public ExpenseSwipeAdapter(Context context, List<Expense> expenses) {
        mContext = context;
        mExpenses = expenses;
    }

    @Override
    public int getCount() {
        return mExpenses.size();
    }

    @Override
    public Object getItem(int position) {
        return mExpenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_show_expense, null);
            viewHolder.categoryLabel = (TextView) convertView.findViewById(R.id.label_item_expense_category);
            viewHolder.amountLabel = (TextView) convertView.findViewById(R.id.label_item_expense_account);
            viewHolder.categoryIcon = (ImageView) convertView.findViewById(R.id.icon_item_expense_category);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Expense expense = mExpenses.get(position);
        viewHolder.categoryLabel.setText(expense.getCategory().getName());
        viewHolder.amountLabel.setText(String.valueOf(expense.getAmount()));
        viewHolder.categoryIcon.setImageResource(expense.getCategory().getImageId());
        return convertView;
    }

    public void setData(List<Expense> expenses) {
        mExpenses = expenses;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView categoryLabel;
        TextView amountLabel;
        ImageView categoryIcon;
    }
}
