package com.u2tzjtne.chatroom.utils;

/**
 * Created by JK on 2017/12/2.
 */

public class ValidUtil {

    //邮箱格式校验
    public static boolean isEmailValid(String email) {
        //TODO 邮箱格式
        return email.contains("@");
    }

    //密码格式校验
    public static boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    //用户名格式校验
    public static boolean isNicknameValid(String nickname) {
        //TODO 用户名格式
        return nickname.length() >= 2;
    }
}
