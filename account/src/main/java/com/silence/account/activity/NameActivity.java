package com.silence.account.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.silence.account.R;
import com.silence.account.application.AccountApplication;
import com.silence.account.dao.UserDao;
import com.silence.account.utils.Constant;
import com.silence.account.utils.T;
import com.silence.account.view.NormalEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class NameActivity extends BaseActivity {

    @Bind(R.id.et_modify_username)
    NormalEditText mEtModifyUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        setTitle(getString(R.string.edit_name));
        showBackwardView(true);
        mEtModifyUsername.setText(BmobUser.getCurrentUser(this).getUsername());
    }

    @Override
    protected Activity getSubActivity() {
        return this;
    }

    @OnClick(R.id.btn_modify_username)
    public void nameClick() {
        final String name = mEtModifyUsername.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    BmobQuery<BmobUser> query = new BmobQuery<>();
                    query.addWhereEqualTo("username", name);
                    query.findObjects(NameActivity.this, new FindListener<BmobUser>() {
                        @Override
                        public void onSuccess(List<BmobUser> object) {
                            if (object.size() > 0) {
                                T.showShort(NameActivity.this, getString(R.string.user_exist));
                            } else {
                                BmobUser newUser = new BmobUser();
                                newUser.setUsername(name);
                                BmobUser bmobUser = BmobUser.getCurrentUser(NameActivity.this);
                                newUser.update(NameActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        T.showShort(NameActivity.this, getString(R.string.update_user_succeed));
                                        new UserDao(NameActivity.this).updateName(name, AccountApplication.sUser.getId());
//                                        EventBus.getDefault().post("update_name");
                                        AccountApplication.sUser.setName(name);
                                        Intent intent = new Intent();
                                        intent.putExtra(Constant.NEW_USERNAME, name);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        T.showShort(NameActivity.this, getString(R.string.update_user_fail));
                                    }
                                });
                            }
                        }

                        //更新用户名
                        @Override
                        public void onError(int code, String msg) {
                            T.showShort(NameActivity.this, getString(R.string.query_fail));
                        }
                    });
                    return null;
                }
            }.execute();
        } else {
            T.showShort(this, getString(R.string.input_username));
        }
    }
}
