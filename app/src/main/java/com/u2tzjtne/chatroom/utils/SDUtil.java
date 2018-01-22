package com.u2tzjtne.chatroom.utils;

import android.os.Environment;

/**
 * Created by JK on 2018/1/20.
 */

public class SDUtil {
    /**
     * 判断SDCard是否存在,并可写
     *
     * @return
     */
    public static boolean checkSDCard() {
        String flag = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(flag);
    }
}
