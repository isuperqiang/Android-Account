package com.silence.account.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Silence on 2016/3/14 0014.
 */
public class ScreenUtils {
    private ScreenUtils() {
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int dp2sp(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

}
