package com.tron.huanxindemo.model.bean;

/**
 * Created by Tron on 2017/2/15.
 *
 * 创建用户bean类
 */

public class UserInfo {

    /*
     * 数据结构: 环信作为一个聊天通道，只需要提供环信ID和密码就够了。
     *
     * 名称	       字段名	      数据类型	  描述
     * 环信ID      username       String      环信ID是环信用户的唯一标识，在 AppKey 的范围内唯一
     * 用户密码    password       String      用户登录环信使用的密码
     *
     * */

    /**
     * 环信ID规则:
     * 当 APP 和环信集成的时候，需要把 APP 系统内的已有用户和新注册的用户和环信集成，
     * 为每个已有用户创建一个环信的账号（环信 ID），并且 APP 有新用户注册的时候，需要同步的在环信中注册。
     *
     * 本文档中可能会交错使用“环信 ID”和“环信用户名”两个术语，但是请注意，这里两个的意思是一样的。
     * 因为一个用户的环信ID和他的在APP中的用户名并不需要一致，只需要有一个明确的对应关系.
     */
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
