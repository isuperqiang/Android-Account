package com.silence.account.activity;

import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.silence.account.R;
import com.silence.account.pojo.User;
import com.silence.account.utils.MD5Encrypt;
import com.silence.account.utils.StringUtils;
import com.silence.account.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.et_reg_username)
    EditText mRegUsername;
    @Bind(R.id.et_reg_email)
    EditText mRegEmail;
    @Bind(R.id.et_reg_pass)
    EditText mRegPass;

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        disPlayBack(true);
        setActionTitle("注册");
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        final String name = mRegUsername.getText().toString().trim();
        final String pass = mRegPass.getText().toString().trim();
        final String email = mRegEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(email)) {
            if (StringUtils.checkEmail(email)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BmobQuery<User> query = new BmobQuery<>();
                        query.addWhereEqualTo("email", email);
                        query.findObjects(RegisterActivity.this, new FindListener<User>() {
                            @Override
                            public void onSuccess(List<User> list) {
                                if (list.size() > 0) {
                                    T.showShort(getApplicationContext(), "该邮箱已注册");
                                } else {
                                    User user = new User();
                                    user.setEmail(email);
                                    //密码要经过md5加密
                                    user.setPassword(MD5Encrypt.MD5Encode(pass));
                                    user.setUsername(name);
                                    user.signUp(RegisterActivity.this, new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            T.showShort(getApplicationContext(), "注册成功");
                                            RegisterActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            T.showShort(getApplicationContext(), "该用户名已被占用");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(int i, String s) {
                                T.showShort(getApplicationContext(), "验证失败");
                            }
                        });
                    }
                }).start();

            } else {
                T.showShort(this, "请输入正确的邮箱号码");
            }
        } else {
            T.showShort(this, "请填写完整信息");
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
