package com.silence.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.silence.account.R;
import com.silence.account.pojo.User;
import com.silence.account.utils.Constant;
import com.silence.account.utils.StringUtils;
import com.silence.account.utils.T;
import com.silence.account.view.NormalEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

/**
 * Created by Silence on 2016/3/7 0007.
 */
public class ForgetPassActivity extends BaseActivity {

    @Bind(R.id.forget_email)
    NormalEditText mForgetEmail;

    @Override
    public void initView() {
        setContentView(R.layout.activity_forget_pass);
        disPlayBack(true);
        setActionTitle("忘记密码");
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_pass_next)
    public void onClick() {
        final String email = mForgetEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            if (StringUtils.checkEmail(email)) {
                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("email", email);
                query.findObjects(this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list.size() > 0) {
                            BmobUser.resetPasswordByEmail(ForgetPassActivity.this, email, new ResetPasswordByEmailListener() {
                                @Override
                                public void onSuccess() {
                                    Intent intent = new Intent(ForgetPassActivity.this, ForgetNextActivity.class);
                                    intent.putExtra(Constant.FORGET_PASS, email);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    T.showShort(getApplicationContext(), "重置密码失败");
                                }
                            });
                        } else {
                            T.showShort(getApplicationContext(), "该邮箱未注册");
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        T.showShort(getApplicationContext(), "请检查网路连接是否开启");
                    }
                });
            } else {
                T.showShort(this, "请输入正确的邮箱号码");
            }
        } else {
            T.showShort(this, "请输入邮箱号码");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
