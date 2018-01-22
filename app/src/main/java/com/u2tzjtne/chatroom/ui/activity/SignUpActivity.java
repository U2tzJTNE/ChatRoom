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
import com.u2tzjtne.chatroom.ui.view.LoadDialog;
import com.u2tzjtne.chatroom.utils.Const;
import com.u2tzjtne.chatroom.utils.MD5Util;
import com.u2tzjtne.chatroom.utils.ValidUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_pass)
    EditText inputPass;
    @BindView(R.id.input_rePass)
    EditText inputRePass;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.input_nickname)
    EditText inputNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    //尝试注册
    private void attemptRegister() {

        // 重置异常提示
        inputEmail.setError(null);
        inputNickname.setError(null);
        inputPass.setError(null);
        inputRePass.setError(null);

        // 获取输入值
        String email = inputEmail.getText().toString();
        String nickname = inputNickname.getText().toString();
        String password = inputPass.getText().toString();
        String rePassword = inputRePass.getText().toString();

        boolean cancel = false;

        //当前焦点
        View focusView = null;

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

        // 验证昵称
        if (!TextUtils.isEmpty(nickname) && !ValidUtil.isNicknameValid(nickname)) {
            inputNickname.setError(getString(R.string.error_invalid_nickname));
            focusView = inputNickname;
            cancel = true;
        }

        // 验证密码输入
        if (!TextUtils.isEmpty(password) && !ValidUtil.isPasswordValid(password)) {
            inputPass.setError(getString(R.string.error_invalid_password));
            focusView = inputPass;
            cancel = true;
        }

        // 确认密码
        if (!TextUtils.isEmpty(rePassword) && !rePassword.equals(password)) {
            inputRePass.setError(getString(R.string.error_invalid_password));
            focusView = inputRePass;
            cancel = true;
        }

        if (cancel) {
            //注册异常时  重置焦点（第一个输入框）
            focusView.requestFocus();
        } else {
            //显示进度条  并尝试注册
            LoadDialog.show(SignUpActivity.this, "正在注册...");
            register(email, password, nickname);
        }

    }

    //开始注册
    private void register(final String email, final String password, String nickname) {
        MyUser user = new MyUser();
        user.setUsername(email);
        user.setEmail(email);
        String pass = MD5Util.getMD5String(password);
        user.setPassword(pass);
        user.setNickname(nickname);
        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                LoadDialog.dismiss(SignUpActivity.this);
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "注册成功，请前往邮箱验证！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("pass", password);
                    setResult(Const.RESULT_CODE, intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "注册失败" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                    inputPass.requestFocus();
                }
            }
        });
    }

    @OnClick({R.id.btn_signup, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                attemptRegister();
                break;
            case R.id.btn_login:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}
