package com.silence.account.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silence on 2015/9/22 0022.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> mData;
    private int mLayoutRes;           //布局id

    public CommonAdapter() {
    }

    public CommonAdapter(List<T> mData, int mLayoutRes) {
        this.mData = mData;
        this.mLayoutRes = mLayoutRes;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView, parent, mLayoutRes
                , position);
        bindView(holder, getItem(position));
        return holder.getItemView();
    }

    public abstract void bindView(ViewHolder holder, T obj);

    //添加一个元素
    public void add(T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }

    //往特定位置，添加一个元素
    public void add(int position, T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if (mData != null) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (mData != null) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }


    public static class ViewHolder {

        private SparseArray<View> mViews;   //存储ListView 的 item中的View
        private View mConvertView;                  //存放convertView
        private int position;               //游标

        //构造方法，完成相关初始化
        private ViewHolder(Context context, ViewGroup parent, int layoutRes, int position) {
            mViews = new SparseArray<>();
            mConvertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
            mConvertView.setTag(this);
            this.position = position;
        }

        //绑定ViewHolder与item
        public static ViewHolder bind(Context context, View convertView, ViewGroup parent,
                                      int layoutRes, int position) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(context, parent, layoutRes, position);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.mConvertView = convertView;
            }
            //   holder.position = position;
            return holder;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            T t = (T) mViews.get(id);
            if (t == null) {
                t = (T) mConvertView.findViewById(id);
                mViews.put(id, t);
            }
            return t;
        }


        /**
         * 获取当前条目
         */
        public View getItemView() {
            return mConvertView;
        }

        /**
         * 获取条目位置
         */
        public int getItemPosition() {
            return position;
        }

        /**
         * 设置文字
         */
        public ViewHolder setText(int id, CharSequence text) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageResource(int id, int drawableRes) {
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
            return this;
        }


        /**
         * 设置点击监听
         */
        public ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
            return this;
        }

        /**
         * 设置可见
         */
        public ViewHolder setVisibility(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        /**
         * 设置标签
         */
        public ViewHolder setTag(int id, Object obj) {
            getView(id).setTag(obj);
            return this;
        }

        //其他方法可自行扩展

    }

}

