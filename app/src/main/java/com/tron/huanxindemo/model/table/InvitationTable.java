package com.tron.huanxindemo.model.table;

/**
 * Created by Tron on 2017/2/16.
 * <p>
 * 创建邀请信息的建表类
 */

public class InvitationTable {

    // 表名
    public static final String TABLE_NAME = "invitation";

    // 邀请人名字
    public static final String COL_USER_NAME = "username";
    // 邀请人环信ID
    public static final String COL_USER_HXID = "userhxid";

    // 群组名称
    public static final String COL_GROUP_NAME = "groupname";
    // 群组ID
    public static final String COL_GROUP_ID = "groupid";

    // 理由
    public static final String COL_REASON = "reason";
    // 状态
    public static final String COL_STATUS = "status";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + COL_USER_HXID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_GROUP_NAME + " text,"
            + COL_GROUP_ID + " text,"
            + COL_REASON + " text,"
            + COL_STATUS + " Integer"
            + ");";
}
