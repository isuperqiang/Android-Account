package com.silence.account.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.silence.account.R;
import com.silence.account.utils.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetNextActivity extends BaseActivity {

    @Bind(R.id.label_send_email)
    TextView mLabelSendEmail;
    @Bind(R.id.label_hint_email)
    TextView mLabelHintEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_next);
        ButterKnife.bind(this);
        showBackwardView(true);
        String email = getIntent().getStringExtra(Constant.FORGET_PASS);
        String validateLabel = "<p>验证邮件已发送至<font color=\"#3f8ddb\">" + email + "</font>，请于1小时内登陆您的邮箱并处理。</p>";
        String hintLabel = "<p>没有收到验证邮件？<br/>&bull; 有可能被误判为垃圾邮件<br/>&bull; 若超过20分钟仍无法接收邮件，请重新提交申请。</p>";
        mLabelSendEmail.setText(Html.fromHtml(validateLabel));
        mLabelHintEmail.setText(Html.fromHtml(hintLabel));
    }

    @Override
    protected Activity getSubActivity() {
        return this;
    }

    @OnClick(R.id.btn_validate)
    public void nextClick() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
