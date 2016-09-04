package com.silence.account.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.silence.account.R;
import com.silence.account.adapter.CommonAdapter;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.model.ExpenseStatistics;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.FormatUtils;
import com.silence.account.view.NoScrollListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Silence on 2016/3/23 0023.
 */
public class PieExpenseFgt extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    @Bind(R.id.label_expense_date_chart)
    TextView mLabelExpenseChart;
    @Bind(R.id.pie_chart)
    PieChartView mPieChart;
    @Bind(R.id.list_expense_chart)
    NoScrollListView mListExpenseChart;
    private PieChartData mPieChartData;
    private ExpenseDao mExpenseDao;
    private int mMonth;
    private int mYear;
    private int[] mArcQueue;
    private List<ExpenseStatistics> mExpenseStatisticses;
    private CommonAdapter mCommonAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar instance = Calendar.getInstance();
        mMonth = instance.get(Calendar.MONTH);
        mYear = instance.get(Calendar.YEAR);
        mExpenseDao = new ExpenseDao(getActivity());
        mPieChartData = new PieChartData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        mPieChartData.setHasCenterCircle(true);
        mPieChartData.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        mPieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        mPieChartData.setCenterText1Color(getResources().getColor(R.color.gray_holo_dark));
        mPieChartData.setCenterText2Color(getResources().getColor(R.color.gray_holo_dark));
        mPieChartData.setHasLabels(true);
        mPieChartData.setHasLabelsOutside(false);
        mPieChartData.setHasLabelsOnlyForSelected(false);
        mPieChart.setValueSelectionEnabled(true);
        mPieChart.setCircleFillRatio(1.0f);
        updateData(DateUtils.getMonthStart(mYear, mMonth), DateUtils.getMonthEnd(mYear, mMonth));
        return view;
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_pie;
    }

    @Override
    protected Fragment getSubFragment() {
        return this;
    }

    private void prepareDataAnimation() {
        List<SliceValue> values = mPieChartData.getValues();
        int mIndex = (int) (Math.random() * values.size());
        mArcQueue = new int[values.size()];
        for (int i = 0, j = values.size(); i < j; i++) {
            if (mIndex >= j - 1) {
                mIndex = 0;
            } else {
                mIndex++;
            }
            mArcQueue[i] = mIndex;
            values.get(i).setTarget(values.get(mIndex).getValue());
        }
    }

    public void updateData(Date start, Date end) {
        String label;
        if (mYear < Calendar.getInstance().get(Calendar.YEAR)) {
            label = DateUtils.date2Str(start, "yyyy年MM月dd日") + " - " + DateUtils.date2Str(end, "dd日");
        } else {
            label = DateUtils.date2Str(start, "MM月dd日") + " - " + DateUtils.date2Str(end, "dd日");
        }
        mLabelExpenseChart.setText(label);
        mPieChart.setOnValueTouchListener(new ValueTouchListener());
        mExpenseStatisticses = mExpenseDao.getPeriodCatSumExpense(start, end, AccountApplication.sUser.getId());
        if (mExpenseStatisticses != null && mExpenseStatisticses.size() > 0) {
            List<SliceValue> sliceValueList = new ArrayList<>(mExpenseStatisticses.size());
            mPieChartData.setCenterText1("总支出");
            float sumExpense = mExpenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
            mPieChartData.setCenterText2(String.valueOf(sumExpense));
            for (int i = 0, j = mExpenseStatisticses.size(); i < j; i++) {
                sliceValueList.add(new SliceValue(mExpenseStatisticses.get(i).getSum(), ChartUtils.nextColor()));
            }
            mPieChartData.setValues(sliceValueList);
        } else {
            mPieChartData.setValues(null);
            mPieChartData.setCenterText1("还没有记录哦~~");
            mPieChartData.setCenterText2("");
        }
        mPieChart.setPieChartData(mPieChartData);
        prepareDataAnimation();
        mPieChart.startDataAnimation();
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<ExpenseStatistics>(mExpenseStatisticses, R.layout.item_chart_statistic) {
                @Override
                public void bindView(ViewHolder holder, ExpenseStatistics obj) {
                    holder.setText(R.id.item_label_amount_chart_list, String.valueOf(obj.getSum()));
                    holder.setText(R.id.item_label_percent_chart_list, String.valueOf(obj.getPercent() + "%"));
                    holder.setImageResource(R.id.item_icon_chart_list, obj.getExpenseCat().getImageId());
                    holder.setText(R.id.item_label_category_chart_list, obj.getExpenseCat().getName());
                }
            };
            mListExpenseChart.setAdapter(mCommonAdapter);
        } else {
            mCommonAdapter.setData(mExpenseStatisticses);
        }
        mPieChart.setFocusable(true);
        mPieChart.setFocusableInTouchMode(true);
        mPieChart.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMonth = Calendar.getInstance().get(Calendar.MONTH);
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("ExpenseDatePickerDialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @OnClick({R.id.icon_expense_chart_left, R.id.label_expense_date_chart, R.id.icon_expense_chart_right})
    public void pieExpenseClick(View view) {
        switch (view.getId()) {
            case R.id.icon_expense_chart_left: {
                mMonth--;
                if (mMonth < 0) {
                    mMonth = mMonth + 12;
                    if ((mMonth + 1) % 12 == 0) {
                        mYear--;
                    }
                }
                updateData(DateUtils.getMonthStart(mYear, mMonth), DateUtils.getMonthEnd(mYear, mMonth));
            }
            break;
            case R.id.label_expense_date_chart: {
                DatePickerDialog dpd = DatePickerDialog.newInstance(this, mYear, mMonth, 1);
                dpd.setStartTitle("开始日期");
                dpd.setEndTitle("结束日期");
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.show(getActivity().getFragmentManager(), "ExpenseDatePickerDialog");
            }
            break;
            case R.id.icon_expense_chart_right: {
                Calendar calendar = Calendar.getInstance();
                if (mYear == calendar.get(Calendar.YEAR) && mMonth >= calendar.get(Calendar.MONTH)) {
                    return;
                }
                mMonth++;
                if (mMonth > 11) {
                    mMonth = mMonth - 12;
                    mYear++;
                }
                updateData(DateUtils.getMonthStart(mYear, mMonth), DateUtils.getMonthEnd(mYear, mMonth));
            }
            break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, monthOfYear);
        start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, yearEnd);
        end.set(Calendar.MONTH, monthOfYearEnd);
        end.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd);
        updateData(start.getTime(), end.getTime());
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            String s = value.toString();
            s = s.substring(s.indexOf("=") + 1, s.indexOf("]"));
            float amount = FormatUtils.formatFloat("########.#", Float.parseFloat(s));
            mPieChartData.setCenterText1(mExpenseStatisticses.get(mArcQueue[arcIndex]).getExpenseCat().getName());
            mPieChartData.setCenterText2(String.valueOf(amount));
        }

        @Override
        public void onValueDeselected() {
        }
    }
}