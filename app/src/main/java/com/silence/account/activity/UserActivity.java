package com.silence.account.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.bmob.BmobPro;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.silence.account.R;
import com.silence.account.application.AccountApplication;
import com.silence.account.pojo.User;
import com.silence.account.utils.Constant;
import com.silence.account.utils.FileUtils;
import com.silence.account.utils.L;
import com.silence.account.utils.T;
import com.silence.account.view.CircleImageView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

public class UserActivity extends BaseActivity {
    private static final int FROM_CAMERA = 0x1;
    private static final int FROM_GALLERY = 0x2;
    private static final int CROP_PHOTO = 0x3;
    private static final int MODIFY_NAME = 0X4;
    private Uri mCropPhoto;
    private Uri mTakePhoto;
    private File mTempPic;
    private String mNewName;
    private String mNewFileName;
    @Bind(R.id.iv_user_photo)
    CircleImageView mIvUserPhoto;
    @Bind(R.id.info_username)
    TextView mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        setTitle("我的资料");
        showBackwardView(true);
        mUsername.setText(BmobUser.getCurrentUser(this).getUsername());
        File photo = new File(BmobPro.getInstance(UserActivity.this).getCacheDownloadDir() +
                File.separator + BmobUser.getCurrentUser(this, User.class).getPicture());
        mIvUserPhoto.setImageBitmap(BitmapFactory.decodeFile(photo.getAbsolutePath()));
    }

    @OnClick({R.id.ll_info_photo, R.id.ll_info_name, R.id.ll_info_pass, R.id.btn_logout})
    public void userClick(View view) {
        switch (view.getId()) {
            case R.id.ll_info_photo: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(new String[]{"拍照", "图库"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                mTakePhoto = Uri.fromFile(new File(getExternalCacheDir() +
                                        File.separator + "IMG_" + System.currentTimeMillis() + ".jpg"));
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhoto);
                                startActivityForResult(intent, FROM_CAMERA);
                            }
                            break;
                            case 1: {
                                getCropUri();
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                intent.putExtra("crop", "true");
                                intent.putExtra("aspectX", 1);// 裁剪框比例
                                intent.putExtra("aspectY", 1);
                                intent.putExtra("outputX", 100);// 输出图片大小
                                intent.putExtra("outputY", 100);
                                intent.putExtra("scale", true);
                                intent.putExtra("return-data", false);
                                intent.putExtra("outputFormat", "JPEG");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropPhoto);
                                intent.putExtra("noFaceDetection", true);
                                startActivityForResult(intent, FROM_GALLERY);
                            }
                            break;
                        }
                    }
                });
                builder.show();
            }
            break;
            case R.id.ll_info_name: {
                startActivityForResult(new Intent(this, NameActivity.class), MODIFY_NAME);
            }
            break;
            case R.id.ll_info_pass: {
                startActivity(new Intent(this, PasswordActivity.class));
            }
            break;
            case R.id.btn_logout: {
                BmobUser.logOut(this);
                AccountApplication.sUser = null;
                startActivity(new Intent(this, LoginActivity.class));
                sendBroadcast(new Intent(Constant.INTENT_FILTER));
                finish();
            }
            break;
        }
    }

    @Override
    protected void onBackward() {
        Intent intent = new Intent();
        if (mNewName != null) {
            intent.putExtra(Constant.NEW_USERNAME, mNewName);
        }
        if (mNewFileName != null) {
            intent.putExtra(Constant.NEW_FILENAME, mNewFileName);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FROM_GALLERY || requestCode == CROP_PHOTO) {
                if (mCropPhoto != null) {
                    Bitmap bitmap = getBitmapFromUri(mCropPhoto);
                    mIvUserPhoto.setImageBitmap(bitmap);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            BmobProFile.getInstance(UserActivity.this).upload(mTempPic.getAbsolutePath(),
                                    new UploadListener() {
                                        @Override
                                        public void onSuccess(final String fileName, String url, BmobFile file) {
                                            L.i("文件上传成功：" + fileName + ",可访问的文件地址：" + file.getUrl());
                                            // fileName ：文件名（带后缀），这个文件名是唯一的，开发者需要记录下该文件名，
                                            // 方便后续下载或者进行缩略图的处理
                                            // url        ：文件地址
                                            // file        :BmobFile文件类型，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                                            //加入本地缓存
                                            FileUtils.writeFile(mTempPic.getAbsolutePath(), BmobPro.getInstance(UserActivity.this)
                                                    .getCacheDownloadDir() + File.separator + fileName);
                                            User user = new User();
                                            user.setPicture(fileName);
                                            user.update(UserActivity.this, BmobUser.getCurrentUser(UserActivity.this).
                                                    getObjectId(), new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    mNewFileName = fileName;
                                                    T.showShort(UserActivity.this, "头像修改成功");
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    T.showShort(UserActivity.this, "头像上传失败");
                                                }
                                            });
                                        }

                                        @Override
                                        public void onProgress(int progress) {
                                        }

                                        @Override
                                        public void onError(int statuscode, String errormsg) {
                                            L.i("文件上传失败：" + errormsg);
                                        }
                                    });
                            return null;
                        }
                    }.execute();
                }
            } else if (requestCode == FROM_CAMERA) {
                cropImageUri(mTakePhoto);
            } else if (requestCode == MODIFY_NAME) {
                mNewName = data.getStringExtra(Constant.NEW_USERNAME);
                mUsername.setText(mNewName);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getCropUri() {
        mTempPic = new File(getExternalCacheDir() + File.separator + "CROP_"
                + System.currentTimeMillis() + ".jpg");
        mCropPhoto = Uri.fromFile(mTempPic);
    }

    private void cropImageUri(Uri uri) {
        getCropUri();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropPhoto);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, CROP_PHOTO);
    }
}
