package com.tron.huanxindemo.model.bean;

/**
 * 作者：Tronzzb on 2017/2/20 15:50.
 * 邮箱：278042465@qq.com
 */

public class PickInfo {

    private UserInfo userInfo;
    private boolean isChecked;

    // 空参构造器
    public PickInfo() {
    }

    public PickInfo(UserInfo userInfo, boolean isChecked) {
        this.userInfo = userInfo;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
