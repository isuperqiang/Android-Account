package com.silence.account.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bmob.BmobPro;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.silence.account.R;
import com.silence.account.dao.ExpenseCatDao;
import com.silence.account.dao.IncomeCatDao;
import com.silence.account.dao.UserDao;
import com.silence.account.pojo.User;
import com.silence.account.utils.L;
import com.silence.account.utils.MD5Encrypt;
import com.silence.account.utils.NetUtils;
import com.silence.account.utils.T;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_login_username)
    EditText mEtLoginName;
    @Bind(R.id.et_login_pass)
    EditText mEtLoginPass;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.label_register, R.id.label_forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login: {
                if (!NetUtils.isConnected(this)) {
                    T.showShort(this, "请检查网络是否连接");
                    return;
                }
                String name = mEtLoginName.getText().toString().trim();
                String pass = mEtLoginPass.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
                    BmobUser.loginByAccount(this, name, MD5Encrypt.MD5Encode(pass), new LogInListener<User>() {
                        @Override
                        public void done(final User user, BmobException e) {
                            if (user != null) {
                                new AsyncTask<Void, Void, Void>() {

                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        UserDao userDao = new UserDao(LoginActivity.this);
                                        userDao.addUser(new User(user.getUsername(), user.getPicture()));
                                        User curUser = userDao.getCurrentUser(user.getUsername());
                                        IncomeCatDao incomeCatDao = new IncomeCatDao(LoginActivity.this);
                                        incomeCatDao.initIncomeCat(curUser);
                                        ExpenseCatDao expenseCatDao = new ExpenseCatDao(LoginActivity.this);
                                        expenseCatDao.initExpensesCat(curUser);
                                        if (user.getPicture() != null) {
                                            String cacheDownloadDir = BmobPro.getInstance(LoginActivity.this).
                                                    getCacheDownloadDir();
                                            File photo = new File(cacheDownloadDir + File.separator
                                                    + user.getPicture());
                                            if (!photo.exists()) {
                                                BmobProFile.getInstance(LoginActivity.this).
                                                        download(user.getPicture(), new DownloadListener() {
                                                            @Override
                                                            public void onSuccess(String fullPath) {
                                                                L.i("下载成功：" + fullPath);
                                                            }

                                                            @Override
                                                            public void onProgress(String localPath, int percent) {
                                                                L.i("download-->onProgress :" + percent);
                                                            }

                                                            @Override
                                                            public void onError(int statuscode, String errormsg) {
                                                                L.i("下载出错：" + statuscode + "--" + errormsg);
                                                            }
                                                        });
                                            }
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    }
                                }.execute();
                            } else {
                                T.showShort(LoginActivity.this, "用户不存在或密码错误");
                            }
                        }
                    });
                } else {
                    T.showShort(this, "请输入用户名和密码");
                }
            }
            break;
            case R.id.label_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.label_forget:
                startActivity(new Intent(this, ForgetPassActivity.class));
                break;
        }
    }

}
