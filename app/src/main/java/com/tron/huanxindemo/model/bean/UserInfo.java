package com.tron.huanxindemo.model.bean;

/**
 * Created by Tron on 2017/2/15.
 *
 * 用户信息
 */

public class UserInfo {

    private String username;
    private String hxid;
    private String nick;
    private String photo;

    // bean类需要有一个空参构造器
    public UserInfo() {
    }

    public UserInfo(String username) {
        this.username = username;
        this.hxid = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
