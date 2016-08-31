package com.silence.account.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.dao.ExpenseCatDao;
import com.silence.account.model.ExpenseCat;
import com.silence.account.utils.T;

import java.util.List;

/**
 * Created by Silence on 2016/3/22 0022.
 */
public class GridExpCatAdapter extends BaseAdapter {
    private List<ExpenseCat> mExpenseCatList;
    private Context mContext;
    private SparseArray<ImageView> mImgCloses;

    public GridExpCatAdapter(Context context, List<ExpenseCat> expenseCats) {
        mContext = context;
        mExpenseCatList = expenseCats;
        mImgCloses = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return mExpenseCatList != null ? mExpenseCatList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mExpenseCatList != null ? mExpenseCatList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.ivCategory = (ImageView) convertView.findViewById(R.id.item_category_icon);
            viewHolder.ivClose = (ImageView) convertView.findViewById(R.id.item_category_close);
            viewHolder.tvCategory = (TextView) convertView.findViewById(R.id.item_category_label);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ExpenseCat expenseCat = mExpenseCatList.get(position);
        String name = expenseCat.getName();
        if (!"添加".equals(name) && !"删除".equals(name)) {
            viewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpenseCatDao expenseCatDao = new ExpenseCatDao(mContext);
                    if (expenseCatDao.delete(expenseCat)) {
                        T.showShort(mContext, "删除成功");
                        mExpenseCatList.remove(position);
                        notifyDataSetChanged();
                        setCloseVisibility(View.GONE);
                        mImgCloses.remove(mImgCloses.size() - 1);
                    } else {
                        T.showShort(mContext, "删除失败");
                        setCloseVisibility(View.GONE);
                    }
                }
            });
            mImgCloses.put(position, viewHolder.ivClose);
        }
        viewHolder.tvCategory.setText(name);
        viewHolder.ivCategory.setImageResource(expenseCat.getImageId());
        return convertView;
    }

    public void setCloseVisibility(int visible) {
        for (int i = 0, j = mImgCloses.size(); i < j; i++) {
            if (visible == View.VISIBLE) {
                mImgCloses.get(i).setVisibility(View.VISIBLE);
            } else {
                mImgCloses.get(i).setVisibility(View.GONE);
            }
        }
    }

    public int getCloseVisibility() {
        return mImgCloses.get(0).getVisibility();
    }

    public void setData(List<ExpenseCat> expenseCats) {
        mExpenseCatList = expenseCats;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView ivClose;
        ImageView ivCategory;
        TextView tvCategory;
    }
}
