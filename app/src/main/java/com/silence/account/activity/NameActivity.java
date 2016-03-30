package com.silence.account.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.silence.account.R;
import com.silence.account.utils.Constant;
import com.silence.account.utils.T;
import com.silence.account.view.NormalEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class NameActivity extends BaseActivity {

    @Bind(R.id.et_modify_username)
    NormalEditText mEtModifyUsername;

    @Override
    public void initView() {
        disPlayBack(true);
        setActionTitle("编辑昵称");
        setContentView(R.layout.activity_name);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEtModifyUsername.setText(BmobUser.getCurrentUser(this).getUsername());
    }

    @OnClick(R.id.btn_modify_username)
    public void onClick() {
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
                                T.showShort(NameActivity.this, "用户名已存在");
                            } else {
                                BmobUser newUser = new BmobUser();
                                newUser.setUsername(name);
                                BmobUser bmobUser = BmobUser.getCurrentUser(NameActivity.this);
                                newUser.update(NameActivity.this, bmobUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        T.showShort(NameActivity.this, "更新用户名成功");
                                        Intent intent = new Intent();
                                        intent.putExtra(Constant.NEW_USERNAME, name);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        T.showShort(NameActivity.this, "更新用户名失败");
                                    }
                                });
                            }
                        }

                        //更新用户名
                        @Override
                        public void onError(int code, String msg) {
                            T.showShort(NameActivity.this, "查询失败");
                        }
                    });
                    return null;
                }
            }.execute();
        } else {
            T.showShort(this, "请填写用户名");
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
