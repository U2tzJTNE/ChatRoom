package com.u2tzjtne.chatroom.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by JK on 2018/1/18.
 */

public class MyUser extends BmobUser {
    private String avatarURL;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
