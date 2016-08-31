package com.silence.account.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.adapter.AllYearStatisticAdapter;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.model.AllYearStatistics;
import com.silence.account.utils.DateUtils;
import com.silence.account.view.NoScrollListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Silence on 2016/4/8 0008.
 */
public class LineFragment extends BaseFragment {

    @Bind(R.id.label_line_date_chart)
    TextView mLabelLineDateChart;
    @Bind(R.id.line_chart)
    LineChartView mLineChart;
    @Bind(R.id.list_line_chart)
    NoScrollListView mListLineChart;
    private ExpenseDao mExpenseDao;
    private IncomeDao mIncomeDao;
    private int mYear;
    private AllYearStatisticAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mExpenseDao = new ExpenseDao(getActivity());
        mIncomeDao = new IncomeDao(getActivity());
        mYear = Calendar.getInstance().get(Calendar.YEAR);
        updateData();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(String message) {
        updateData();
    }

    private void updateData() {
        mLabelLineDateChart.setText(mYear + "年");
        int numberOfLines = 3;
        int numberOfPoints = 12;
        float[][] randomNumbersTab = new float[numberOfLines][numberOfPoints];
        List<AllYearStatistics> allYearStatisticses = new ArrayList<>();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int j = 0; j < numberOfPoints; j++) {
            Date start = DateUtils.getMonthStart(mYear, j);
            Date end = DateUtils.getMonthEnd(mYear, j);
            float sumIncome = mIncomeDao.getPeriodSumIncome(start, end, AccountApplication.sUser.getId());
            float sumExpense = mExpenseDao.getPeriodSumExpense(start, end, AccountApplication.sUser.getId());
            float sumBalance = sumIncome - sumExpense;
            randomNumbersTab[0][j] = sumIncome;
            randomNumbersTab[1][j] = sumExpense;
            randomNumbersTab[2][j] = sumBalance;
            if (mYear == currentYear && j <= currentMonth) {
                allYearStatisticses.add(new AllYearStatistics(j + 1, sumIncome, sumExpense, sumBalance));
            }
            if (mYear < currentYear) {
                allYearStatisticses.add(new AllYearStatistics(j + 1, sumIncome, sumExpense, sumBalance));
            }
        }
        List<Line> lines = new ArrayList<>();
        List<PointValue> values;
        float top = randomNumbersTab[0][0];
        float bottom = randomNumbersTab[0][0];
        Line line;
        for (int i = 0; i < numberOfLines; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; j++) {
                values.add(new PointValue(j + 1, randomNumbersTab[i][j]));
                if (top < randomNumbersTab[i][j]) {
                    top = randomNumbersTab[i][j];
                }
                if (bottom > randomNumbersTab[i][j]) {
                    bottom = randomNumbersTab[i][j];
                }
            }
            line = new Line(values);
            line.setStrokeWidth(1);
            line.setPointRadius(3);
            line.setColor(ChartUtils.COLORS[i]);
            line.setHasLabelsOnlyForSelected(true);
            line.setPointColor(ChartUtils.COLORS[i]);
            lines.add(line);
        }
        LineChartData data = new LineChartData(lines);
        Axis axisX = new Axis();
        axisX.setName(getString(R.string.month));
        axisX.setTextColor(Color.BLACK);
        axisX.setLineColor(Color.BLACK);
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i <= numberOfPoints; i++) {
            axisValues.add(new AxisValue(i).setLabel(i + "月"));
        }
        axisX.setValues(axisValues);
        Axis axisY = new Axis();
        axisY.setHasLines(true);
        axisY.setName(getString(R.string.amount) + "/(元)");
        axisY.setTextColor(Color.BLACK);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        mLineChart.setLineChartData(data);
        mLineChart.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(mLineChart.getMaximumViewport());
        v.bottom = bottom;
        v.top = top;
        v.left = 0;
        v.right = numberOfPoints + 2;
        v.inset(1, 0);
        mLineChart.setMaximumViewport(v);
        mLineChart.setCurrentViewport(v);
        if (mAdapter == null) {
            mAdapter = new AllYearStatisticAdapter(getActivity(), allYearStatisticses);
            mListLineChart.setAdapter(mAdapter);
        } else {
            mAdapter.setData(allYearStatisticses);
        }
        mLineChart.setFocusable(true);
        mLineChart.setFocusableInTouchMode(true);
        mLineChart.requestFocus();
    }

    @OnClick({R.id.icon_line_chart_left, R.id.icon_line_chart_right})
    public void lineClick(View view) {
        switch (view.getId()) {
            case R.id.icon_line_chart_left: {
                mYear--;
                updateData();
            }
            break;
            case R.id.icon_line_chart_right: {
                if (mYear < Calendar.getInstance().get(Calendar.YEAR)) {
                    mYear++;
                    updateData();
                }
            }
            break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_line;
    }

    @Override
    protected Fragment getViewRoot() {
        return this;
    }
}
