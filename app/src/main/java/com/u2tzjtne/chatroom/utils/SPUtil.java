package com.u2tzjtne.chatroom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.u2tzjtne.chatroom.app.MyApp;

/**
 * Created by JK on 2017/10/9.
 * <p>
 * SharedPreferences工具类
 */

public class SPUtil {
    private static Context context = MyApp.getContext();
    private static SharedPreferences sp = context.getSharedPreferences("ChatRoom", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor edit = sp.edit();

    public static void putBoolean(String key, boolean value) {
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static void putInt(String key, int value) {
        edit.putInt(key, value);
        edit.apply();
    }

    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public static void putString(String key, String value) {
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }
}
