package com.u2tzjtne.chatroom.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.u2tzjtne.chatroom.utils.Const;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import cn.bmob.v3.Bmob;


/**
 * Created by JK on 2017/11/14.
 */

public class MyApp extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
        //初始化 Bmob
        Bmob.initialize(this, Const.BMOB_APPID);
        //初始化 Emoji键盘
        EmojiManager.install(new IosEmojiProvider());
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
