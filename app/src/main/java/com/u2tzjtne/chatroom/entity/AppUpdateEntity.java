package com.u2tzjtne.chatroom.entity;

/**
 * Created by JK on 2018/1/12.
 * <p>
 * 版本更新实体类
 */

public class AppUpdateEntity {

    /**
     * versionCode : 2
     * des : 测试信息hhhhhhhhhhhhh
     * apkUrl : https://dl.wandoujia.com/files/jupiter/latest/wandoujia-web_seo_baidu_homepage.apk
     * isForcedUpdate : true
     */

    private int versionCode;
    private String des;
    private String apkUrl;
    private boolean isForcedUpdate;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public boolean isIsForcedUpdate() {
        return isForcedUpdate;
    }

    public void setIsForcedUpdate(boolean isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }
}
