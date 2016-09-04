package com.silence.account.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.silence.account.R;
import com.silence.account.model.User;
import com.silence.account.utils.Constant;
import com.silence.account.utils.StringUtils;
import com.silence.account.utils.T;
import com.silence.account.view.NormalEditText;

import java.util.List;

import butterknife.Bind;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        setTitle(getString(R.string.find_password));
        showBackwardView(true);
    }

    @Override
    protected Activity getSubActivity() {
        return this;
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
                                    T.showShort(getApplicationContext(), getString(R.string.reset_pass_fail));
                                }
                            });
                        } else {
                            T.showShort(getApplicationContext(), getString(R.string.unregister_email));
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        T.showShort(getApplicationContext(), getString(R.string.check_net_connect));
                    }
                });
            } else {
                T.showShort(this, getString(R.string.input_right_email));
            }
        } else {
            T.showShort(this, getString(R.string.input_email));
        }
    }

}
