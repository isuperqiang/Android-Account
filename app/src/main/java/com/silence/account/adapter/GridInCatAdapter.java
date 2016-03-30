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
import com.silence.account.dao.IncomeCatDao;
import com.silence.account.pojo.IncomeCat;
import com.silence.account.utils.T;

import java.util.List;

/**
 * Created by Silence on 2016/3/22 0022.
 */
public class GridInCatAdapter extends BaseAdapter {
    private List<IncomeCat> mIncomeCats;
    private Context mContext;
    private SparseArray<ImageView> mImgCloses;

    public GridInCatAdapter(Context context, List<IncomeCat> incomeCats) {
        mContext = context;
        mIncomeCats = incomeCats;
        mImgCloses = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return mIncomeCats != null ? mIncomeCats.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mIncomeCats != null ? mIncomeCats.get(position) : null;
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
        final IncomeCat incomeCat = mIncomeCats.get(position);
        String name = incomeCat.getName();
        if (!"添加".equals(name) && !"删除".equals(name)) {
            viewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IncomeCatDao incomeCatDao = new IncomeCatDao(mContext);
                    if (incomeCatDao.delete(incomeCat)) {
                        T.showShort(mContext, "删除成功");
                        mIncomeCats.remove(position);
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
        viewHolder.ivCategory.setImageResource(incomeCat.getImageId());
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

    public void setData(List<IncomeCat> incomeCats){
        mIncomeCats=incomeCats;
        notifyDataSetChanged();
    }

    public int getCloseVisibility() {
        return mImgCloses.get(0).getVisibility();
    }

    static class ViewHolder {
        ImageView ivClose;
        ImageView ivCategory;
        TextView tvCategory;
    }
}
