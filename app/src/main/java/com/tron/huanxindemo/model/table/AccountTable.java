package com.tron.huanxindemo.model.table;

/**
 * Created by Tron on 2017/2/15.
 */

public class AccountTable {

//    create table afu(id text primary key,name text);
//
//    insert into afu(id,name) values(1,"小泽");
//
//    insert into afu(id,name) values(2,"小CANG");
//
//    insert into afu(id,name) values(3,"小龙");
//
//    delete from afu where id = 3;
//
//    select * from afu where id = 1;
//
//    update afu set name = "老王" where id = 2

    // 表名
    public static final String TABLE_NAME = "userinfo";
    // 用户名
    public static final String COL_USER_NAME = "username";
    // 环信id
    public static final String COL_USER_HXID = "hxid";
    // 昵称
    public static final String COL_USER_NICK = "nick";
    //
    public static final String COL_USER_PHOTO = "photo";

    // 创建表, 初始化数据
    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + COL_USER_HXID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_USER_NICK + " text,"
            + COL_USER_PHOTO + " text"
            + ");";
}
