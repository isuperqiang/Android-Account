package com.silence.account.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bmob.BmobPro;
import com.silence.account.R;
import com.silence.account.activity.UserActivity;
import com.silence.account.pojo.User;
import com.silence.account.utils.Constant;
import com.silence.account.view.CircleImageView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created by Silence on 2016/3/7 0007.
 * 我的模块
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.iv_user_photo)
    CircleImageView mIvUserPhoto;
    @Bind(R.id.me_username)
    TextView mUsername;
    private Context mContext;
    private static final int UPDATE_USER = 0X1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        File photo = new File(BmobPro.getInstance(mContext).getCacheDownloadDir() + File.separator +
                BmobUser.getCurrentUser(mContext, User.class).getPicture());
        if (photo.exists()) {
            mIvUserPhoto.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
        }
        mUsername.setText(BmobUser.getCurrentUser(mContext).getUsername());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_me_user, R.id.ll_me_setting, R.id.ll_me_reminder, R.id.ll_me_share, R.id.ll_me_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_me_user:
                startActivityForResult(new Intent(mContext, UserActivity.class), UPDATE_USER);
                break;
            case R.id.ll_me_setting:
                break;
            case R.id.ll_me_reminder:
                break;
            case R.id.ll_me_share:
                break;
            case R.id.ll_me_about:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == UPDATE_USER) {
            String newFile = data.getStringExtra(Constant.NEW_FILENAME);
            if (newFile != null) {
                mIvUserPhoto.setImageBitmap(BitmapFactory.decodeFile(BmobPro.getInstance(mContext)
                        .getCacheDownloadDir() + File.separator + newFile));
            }
            String newName = data.getStringExtra(Constant.NEW_USERNAME);
            if (newName != null) {
                mUsername.setText(newName);
            }
        }
    }
}
