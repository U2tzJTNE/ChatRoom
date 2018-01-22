package com.u2tzjtne.chatroom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.u2tzjtne.chatroom.R;
import com.u2tzjtne.chatroom.entity.MyUser;
import com.u2tzjtne.chatroom.entity.UserModel;
import com.u2tzjtne.chatroom.ui.view.LoadDialog;
import com.u2tzjtne.chatroom.utils.Const;
import com.u2tzjtne.chatroom.utils.MD5Util;
import com.u2tzjtne.chatroom.utils.ValidUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_signup)
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    /**
     * 屏蔽返回键
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //设置输入框的值
    @Override
    protected void onResume() {
        super.onResume();
        MyUser user = UserModel.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                inputEmail.setText(email);
            }
        }
    }

    /**
     * 尝试登录
     */
    private void attemptLogin() {

        // 重置异常提示
        inputEmail.setError(null);
        inputPassword.setError(null);

        //获取输入值
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 验证密码输入
        if (!TextUtils.isEmpty(password) && !ValidUtil.isPasswordValid(password)) {
            inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = inputPassword;
            cancel = true;
        }

        // 验证邮件地址
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.error_field_required));
            focusView = inputEmail;
            cancel = true;
        } else if (!ValidUtil.isEmailValid(email)) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            focusView = inputEmail;
            cancel = true;
        }

        if (cancel) {
            //登录异常时  重置焦点（第一个输入框）
            focusView.requestFocus();
        } else {
            //显示进度条  并尝试登录
            LoadDialog.show(this, "正在登录...");
            login(email, password);
        }
    }


    //开始登录
    private void login(final String email, final String password) {
        final MyUser user = new MyUser();
        user.setUsername(email);
        user.setPassword(MD5Util.getMD5String(password));
        user.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                LoadDialog.dismiss(LoginActivity.this);
                if (e == null) {
                    if (myUser.getEmailVerified()) {//邮箱是否验证
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                        finish();
                    } else {
                        //未验证清除缓存对象
                        Toast.makeText(LoginActivity.this, "请验证邮箱后登录", Toast.LENGTH_SHORT).show();
                        UserModel.getInstance().logout();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    inputPassword.requestFocus();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE && resultCode == Const.RESULT_CODE) {
            inputPassword.setText(data.getStringExtra("pass"));
        }
    }

    @OnClick({R.id.btn_login, R.id.btn_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_signup:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, Const.REQUEST_CODE);
                break;
        }
    }
}
