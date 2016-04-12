package com.silence.account.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.utils.FormatUtils;
import com.silence.account.utils.T;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Silence on 2016/3/7 0007.
 */
public class AccountFragment extends BaseFragment {
    @Bind(R.id.et_deposit_amount)
    EditText mEtDepositAmount;
    @Bind(R.id.radio_gp_deposit)
    RadioGroup mRadioGpDeposit;
    @Bind(R.id.et_deposit_year)
    EditText mEtDepositYear;
    @Bind(R.id.et_deposit_rate)
    EditText mEtDepositRate;
    @Bind(R.id.tv_deposit_interest)
    TextView mTvDepositInterest;
    @Bind(R.id.tv_deposit_sum)
    TextView mTvDepositSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        mRadioGpDeposit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_deposit_regular) {
                    mEtDepositRate.setText("2.5");
                } else if (checkedId == R.id.radio_deposit_demand) {
                    mEtDepositRate.setText("0.35");
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_calculate)
    public void onClick() {
        String strAmount = mEtDepositAmount.getText().toString().trim();
        if (TextUtils.isEmpty(strAmount)) {
            T.showShort(getActivity(), "请输入金额");
            return;
        }
        if (mRadioGpDeposit.getCheckedRadioButtonId() == -1) {
            T.showShort(getActivity(), "请选择类型");
            return;
        }
        String strYear = mEtDepositYear.getText().toString().trim();
        if (TextUtils.isEmpty(strYear)) {
            T.showShort(getActivity(), "请输入年限");
            return;
        }
        String strRate = mEtDepositRate.getText().toString().trim();
        if (TextUtils.isEmpty(strRate)) {
            T.showShort(getActivity(), "请输入利率");
            return;
        }
        float amount = Float.parseFloat(strAmount);
        float interest = amount * Float.parseFloat(strRate) * Float.parseFloat(strYear) / 100;
        interest = FormatUtils.formatFloat("#.00", interest);
        float sum = interest + amount;
        mTvDepositInterest.setText(interest + "元");
        mTvDepositSum.setText(sum + "元");
    }

    public void clearNumber() {
        mEtDepositRate.setText("");
        mEtDepositAmount.setText("");
        mEtDepositYear.setText("");
        mTvDepositSum.setText("0.00元");
        mTvDepositInterest.setText("0.00元");
    }
}
