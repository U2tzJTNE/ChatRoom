package com.u2tzjtne.chatroom.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.u2tzjtne.chatroom.R;
import com.u2tzjtne.chatroom.entity.MyUser;
import com.u2tzjtne.chatroom.entity.UserModel;
import com.u2tzjtne.chatroom.ui.view.LoadDialog;
import com.u2tzjtne.chatroom.utils.PhotoUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.account_activity_avatar)
    ImageView icAvatar;
    @BindView(R.id.item_avatar)
    LinearLayout itemAvatar;
    @BindView(R.id.account_activity_nickname)
    TextView tvNickname;
    @BindView(R.id.item_nickname)
    LinearLayout itemNickname;
    @BindView(R.id.account_activity_email)
    TextView tvEmail;
    @BindView(R.id.item_email)
    LinearLayout itemEmail;
    @BindView(R.id.item_update_pass)
    LinearLayout itemUpdatePass;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    private PhotoUtil photoUtil;
    private Uri selectUri;
    private Context mContext;

    public static final int REQUEST_PERMISSIONS_CAMERA = 101;
    public static final int REQUEST_PERMISSIONS_GALLERY = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtil.INTENT_CROP:
            case PhotoUtil.INTENT_TAKE:
            case PhotoUtil.INTENT_SELECT:
                photoUtil.onActivityResult(AccountActivity.this, requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyUser user = UserModel.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String nickname = user.getNickname();
            String avatarURL = user.getAvatarURL();
            if (email != null) {
                tvEmail.setText(email);
            }
            if (nickname != null) {
                tvNickname.setText(nickname);
            }
            if (avatarURL != null) {
                Glide.with(AccountActivity.this).load(avatarURL).into(icAvatar);
            } else {
                icAvatar.setImageResource(R.drawable.splash);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.item_avatar, R.id.item_nickname, R.id.item_email, R.id.item_update_pass, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_avatar://头像item
                showDialog();
                break;
            case R.id.item_nickname://昵称item
                break;
            case R.id.item_email://邮箱item
                break;
            case R.id.item_update_pass://更改密码
                break;
            case R.id.btn_logout://退出登录
                UserModel.getInstance().logout();
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    private void showDialog() {
        String[] items = new String[]{"相册", "拍照"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0://相册
                        int storage1 = ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (storage1 != PackageManager.PERMISSION_GRANTED) {
                            //获取权限
                            if (Build.VERSION.SDK_INT >= 23) {
                                getPermission(REQUEST_PERMISSIONS_GALLERY);
                            } else {
                                Toast.makeText(mContext, "请去设置里面打开存储权限", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            photoUtil.selectPicture(AccountActivity.this);
                        }
                        break;
                    case 1://拍照
                        int camera = ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.CAMERA);
                        int storage = ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (camera != PackageManager.PERMISSION_GRANTED || storage != PackageManager.PERMISSION_GRANTED) {
                            //获取权限
                            if (Build.VERSION.SDK_INT >= 23) {
                                getPermission(REQUEST_PERMISSIONS_CAMERA);
                            } else {
                                Toast.makeText(mContext, "请去设置里面打开相机和存储权限", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            photoUtil.takePicture(AccountActivity.this);
                        }
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermission(final int requestCode) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CAMERA:
                //之前拒绝过
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                        || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //向用户解释
                    new AlertDialog.Builder(mContext)
                            .setMessage("您需要在设置里打开相机和存储权限")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create().show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                }
                break;
            case REQUEST_PERMISSIONS_GALLERY://只获取存储权限
                //之前拒绝过
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //向用户解释
                    new AlertDialog.Builder(mContext)
                            .setMessage("您需要在设置里打开外置存储权限")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create().show();

                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                }
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //允许  开始调用相机
                    photoUtil.takePicture(AccountActivity.this);
                } else {
                    // 拒绝
                    Toast.makeText(AccountActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSIONS_GALLERY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许  开始调用相册
                    photoUtil.selectPicture(AccountActivity.this);
                } else {
                    // 拒绝
                    Toast.makeText(AccountActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //裁剪后返回
    private void setPortraitChangeListener() {
        photoUtil = new PhotoUtil(new PhotoUtil.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    selectUri = uri;
                    //显示加载
                    LoadDialog.show(mContext);
                    final BmobFile bmobFile = new BmobFile(new File(uri.getPath()));
                    //TODO 上传出错
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            //上传失败
                            LoadDialog.dismiss(mContext);
                            if (e == null) {
                                //上传成功后更新头像链接
                                updateAccountInfo(new String[]{null, null, bmobFile.getFileUrl()});
                            } else {
                                Toast.makeText(mContext, "上传失败:" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    //更新资料
    private boolean updateAccountInfo(String[] items) {
        final boolean[] succeed = {false};
        String nickname = items[0];
        final String email = items[1];
        String avatarURL = items[2];
        LoadDialog.show(AccountActivity.this);
        BmobUser user = UserModel.getInstance().getCurrentUser();
        MyUser myUser = new MyUser();
        if (nickname != null) {
            myUser.setNickname(nickname);
        }
        if (email != null) {
            myUser.setEmail(email);
        }
        if (avatarURL != null) {
            myUser.setAvatarURL(avatarURL);
        }
        myUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LoadDialog.dismiss(AccountActivity.this);
                if (e == null) {
                    if (email != null) {
                        UserModel.getInstance().logout();
                        Toast.makeText(AccountActivity.this, "更新成功,请验证后登录", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AccountActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    }
                    succeed[0] = true;
                } else {
                    succeed[0] = false;
                    Toast.makeText(AccountActivity.this, "更新失败,代码：" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return succeed[0];
    }
}
