package com.silence.account.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.silence.account.R;

/**
 * Created by Silence on 2016/3/6 0006.
 * 自定义EditText,，在内容右边显示清空按钮
 */
public class NormalEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {
    private Drawable mDrawable;
    private boolean mHasFocus;

    public NormalEditText(Context context) {
        this(context, null);
    }

    public NormalEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public NormalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//         获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mDrawable = getCompoundDrawables()[2];
        if (mDrawable == null) {
            mDrawable = getResources().getDrawable(R.mipmap.icon_big_close);
        }

        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     */
    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) &&
                        (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (mHasFocus) {
            setClearIconVisible(s.length() > 0);
        }
    }

}
