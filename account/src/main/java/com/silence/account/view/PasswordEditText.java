package com.silence.account.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.silence.account.R;

/**
 * Created by Silence on 2016/3/6 0006.
 */
public class PasswordEditText extends EditText implements TextWatcher {
    private Drawable mHide;
    private Drawable mShow;
    private boolean mVisible;
    private int mCount;

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PasswordEditText(Context context) {
        this(context, null);
    }


    private void init() {
//         获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mHide = getCompoundDrawables()[2];
        if (mHide == null) {
            mHide = getResources().getDrawable(R.mipmap.password_hide);
        }
        mShow = getResources().getDrawable(R.mipmap.password_show);
        mHide.setBounds(0, 0, mHide.getIntrinsicWidth(), mHide.getIntrinsicHeight());
        mShow.setBounds(0, 0, mShow.getIntrinsicWidth(), mShow.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mHide, getCompoundDrawables()[3]);
        addTextChangedListener(this);
    }

    /**
     * 设置显示与隐藏的图标，调用setCompoundDrawables为EditText绘制上去
     */
    private void changeDrawable(boolean visible) {
        Drawable right = visible ? mShow : mHide;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) &&
                        (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    if (mVisible) {
                        mVisible = false;
                        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    } else {
                        mVisible = true;
                        setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    changeDrawable(mVisible);
                    setSelection(mCount);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        mCount = s.length();
    }
}
