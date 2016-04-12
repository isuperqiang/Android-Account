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
import com.silence.account.utils.DateUtils;

import java.util.List;

/**
 * Created by Silence on 2016/3/14 0014.
 */
public class IncomeSwipeAdapter extends BaseSwipeListAdapter {
    private Context mContext;
    private List<Income> mIncomes;
    private boolean mToday;
    private int mDay;

    public IncomeSwipeAdapter(Context context, List<Income> Incomes, boolean today) {
        mContext = context;
        mIncomes = Incomes;
        mToday = today;
        if (mIncomes != null && mIncomes.size() > 0) {
            mDay = Integer.parseInt(DateUtils.date2Str(mIncomes.get(0).getDate(), "dd"));
        }
    }

    @Override
    public int getCount() {
        return mIncomes == null ? 0 : mIncomes.size();
    }

    @Override
    public Object getItem(int position) {
        return mIncomes == null ? null : mIncomes.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_show_income, parent, false);
            viewHolder.categoryLabel = (TextView) convertView.findViewById(R.id.label_item_income_category);
            viewHolder.amountLabel = (TextView) convertView.findViewById(R.id.label_item_income_account);
            viewHolder.categoryIcon = (ImageView) convertView.findViewById(R.id.icon_item_income_category);
            viewHolder.dateLabel = (TextView) convertView.findViewById(R.id.label_item_income_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Income income = mIncomes.get(mIncomes.size() - 1 - position);
        String date = DateUtils.date2Str(income.getDate(), "MM/dd");
        int day = Integer.parseInt(date.substring(date.indexOf("/") + 1));
        if ((!mToday && mDay != day) || position == 0) {
            viewHolder.dateLabel.setVisibility(View.VISIBLE);
            viewHolder.dateLabel.setText(date);
            mDay = day;
        }
        viewHolder.categoryLabel.setText(income.getCategory().getName());
        viewHolder.amountLabel.setText(String.valueOf(income.getAmount()));
        viewHolder.categoryIcon.setImageResource(income.getCategory().getImageId());
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
        TextView dateLabel;
    }
}
