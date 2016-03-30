package com.silence.account.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipeListAdapter;
import com.silence.account.R;
import com.silence.account.pojo.Income;

import java.util.List;

/**
 * Created by Silence on 2016/3/14 0014.
 */
public class IncomeSwipeAdapter extends BaseSwipeListAdapter {
    private Context mContext;
    private List<Income> mIncomes;

    public IncomeSwipeAdapter(Context context, List<Income> Incomes) {
        mContext = context;
        mIncomes = Incomes;
    }

    @Override
    public int getCount() {
        return mIncomes.size();
    }

    @Override
    public Object getItem(int position) {
        return mIncomes.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_show_income, null);
            viewHolder.categoryLabel = (TextView) convertView.findViewById(R.id.label_item_income_category);
            viewHolder.amountLabel = (TextView) convertView.findViewById(R.id.label_item_income_account);
            viewHolder.categoryIcon = (ImageView) convertView.findViewById(R.id.icon_item_income_category);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Income Income = mIncomes.get(position);
        viewHolder.categoryLabel.setText(Income.getCategory().getName());
        viewHolder.amountLabel.setText(String.valueOf(Income.getAmount()));
        viewHolder.categoryIcon.setImageResource(Income.getCategory().getImageId());
        return convertView;
    }

    public void setData(List<Income> Incomes) {
        mIncomes = Incomes;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView categoryLabel;
        TextView amountLabel;
        ImageView categoryIcon;
    }
}
