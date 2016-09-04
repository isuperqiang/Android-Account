package com.silence.account.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobPro;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.silence.account.R;
import com.silence.account.activity.AboutActivity;
import com.silence.account.activity.LoginActivity;
import com.silence.account.activity.MainActivity;
import com.silence.account.activity.UserActivity;
import com.silence.account.application.AccountApplication;
import com.silence.account.model.User;
import com.silence.account.receiver.AlarmReceiver;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DBOpenHelper;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.T;
import com.silence.account.view.CircleImageView;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Silence on 2016/3/7 0007.
 * 我的模块
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.iv_user_photo)
    CircleImageView mIvUserPhoto;
    @Bind(R.id.me_username)
    TextView mUsername;
    private MainActivity mContext;
    private static final int UPDATE_USER = 0X1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        File photo = new File(BmobPro.getInstance(mContext).getCacheDownloadDir() + File.separator +
                BmobUser.getCurrentUser(mContext, User.class).getPicture());
        if (photo.exists()) {
            mIvUserPhoto.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
        }
        mUsername.setText(BmobUser.getCurrentUser(mContext).getUsername());
        return view;
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected Fragment getSubFragment() {
        return this;
    }

    SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            Intent intent = new Intent(mContext, AlarmReceiver.class);
            intent.setAction(Constant.ACTION_ALARM);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            Toast.makeText(mContext, "闹钟将在" + DateUtils.date2Str(date, "MM-dd HH:mm")
                    + "发出提醒", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDateTimeCancel() {

        }
    };

    @OnClick({R.id.ll_me_user, R.id.ll_me_reminder, R.id.ll_me_share,
            R.id.ll_me_about, R.id.ll_me_check, R.id.ll_me_init})
    public void mineClick(View view) {
        switch (view.getId()) {
            case R.id.ll_me_user: {
                startActivityForResult(new Intent(mContext, UserActivity.class), UPDATE_USER);
            }
            break;
            case R.id.ll_me_reminder: {
                new SlideDateTimePicker.Builder(mContext.getSupportFragmentManager())
                        .setListener(listener)
                        .setIs24HourTime(true)
                        .setIndicatorColor(Color.parseColor("#f6a844"))
                        .build()
                        .show();
            }
            break;
            case R.id.ll_me_init: {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("警告");
                builder.setCancelable(true);
                builder.setMessage(" 初始化将删除所有的软件记录并恢复软件的最初设置，你确定这么做吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //在这里初始化所有数据表，并退出登录
                        DBOpenHelper.getInstance(mContext).dropTable();
                        BmobUser.logOut(mContext);
                        AccountApplication.sUser = null;
                        startActivity(new Intent(mContext, LoginActivity.class));
                        mContext.finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
            break;
            case R.id.ll_me_check: {
                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setTitle("正在检查新版本");
                dialog.setMessage("请稍后...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        T.showShort(mContext, "已是最新版本，无需更新！");
                    }
                });
                dialog.show();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        dialog.cancel();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, Constant.DELAY_TIME);
            }
            break;
            case R.id.ll_me_share: {
                OnekeyShare oks = new OnekeyShare();
                oks.disableSSOWhenAuthorize();
                oks.setTitle("分享一款好用的记账软件");
                oks.setTitleUrl("http://weibo.com/SilenceLiu93");
                oks.setText("亲们，给大家推荐一款记账软件，好漂亮的界面，记账好简单，超赞的！");
                oks.setImageUrl("http://d.picphotos.baidu.com/album/s%3D740%3Bq%3D90/sign=22e18184902bd40746c7d1f94bb2ef6c/37d3d539b6003af31142c3cc322ac65c1138b6a5.jpg");
                oks.setSite(getString(R.string.app_name));
                oks.setSiteUrl("http://weibo.com/SilenceLiu93");
                oks.setUrl("http://weibo.com/SilenceLiu93");
                oks.show(mContext);
            }
            break;
            case R.id.ll_me_about: {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
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
