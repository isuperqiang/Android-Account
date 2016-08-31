package com.silence.account.model;

/**
 * Created by Silence on 2016/3/24 0024.
 */
public class ExpenseStatistics {
    private ExpenseCat mExpenseCat;
    private float mSum;
    private float mPercent;

    public ExpenseStatistics() {
    }

    public ExpenseStatistics(ExpenseCat expenseCat, float sum, float percent) {
        mExpenseCat = expenseCat;
        mSum = sum;
        mPercent = percent;
    }

    public ExpenseCat getExpenseCat() {
        return mExpenseCat;
    }

    public void setExpenseCat(ExpenseCat expenseCat) {
        mExpenseCat = expenseCat;
    }

    public float getSum() {
        return mSum;
    }

    public void setSum(float sum) {
        mSum = sum;
    }

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float percent) {
        mPercent = percent;
    }

}
