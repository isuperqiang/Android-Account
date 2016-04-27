package com.silence.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.silence.account.R;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.IncomeCatDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.dao.InvestDao;
import com.silence.account.pojo.Income;
import com.silence.account.pojo.IncomeCat;
import com.silence.account.pojo.Invest;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.T;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvestActivity extends BaseActivity {

    @Bind(R.id.et_invest_amount)
    EditText mEtInvestAmount;
    @Bind(R.id.spinner_invest)
    Spinner mSpinnerInvest;
    @Bind(R.id.et_invest_year)
    EditText mEtInvestYear;
    @Bind(R.id.et_invest_rate)
    EditText mEtInvestRate;
    private String mType;
    private float mRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        ButterKnife.bind(this);
        showBackwardView(true);
        setTitle("记录投资");
        mRemain = getIntent().getFloatExtra(Constant.INVEST, 0);
        mType = (String) mSpinnerInvest.getSelectedItem();
        mSpinnerInvest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btn_add_invest)
    public void investClick() {
        String amount = mEtInvestAmount.getText().toString().trim();
        float amount1 = Float.parseFloat(amount);
        if (TextUtils.isEmpty(amount)) {
            T.showShort(this, "请输入金额");
            return;
        } else {
            if (mRemain < amount1) {
                T.showShort(this, "结余金额不足");
                return;
            }
        }
        String year = mEtInvestYear.getText().toString().trim();
        if (TextUtils.isEmpty(year)) {
            T.showShort(this, "请输入期限");
            return;
        }
        String rate = mEtInvestRate.getText().toString().trim();
        if (TextUtils.isEmpty(rate)) {
            T.showShort(this, "请输入利率");
            return;
        }
        float rate1 = Float.parseFloat(rate);
        int year1 = Integer.parseInt(year);
        float earning = amount1 * year1 * rate1 / 100;
        Invest invest = new Invest(amount1, year1, rate1, mType, earning, new Date(), AccountApplication.sUser);
        IncomeCat incomeCat = new IncomeCatDao(this).findInvest();
        Income income = new Income(new Date(), earning, incomeCat, AccountApplication.sUser,
                DateUtils.date2Str(new Date(), "yyyy-MM-dd") + ", 投资" + amount + "元, 期限" + year + "年");
        if (new InvestDao(this).addInvest(invest) && new IncomeDao(this).addIncome(income)) {
            T.showShort(this, "添加成功");
            Intent intent = new Intent();
            intent.putExtra(Constant.INVEST, amount);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            T.showShort(this, "添加失败");
        }
    }
}
