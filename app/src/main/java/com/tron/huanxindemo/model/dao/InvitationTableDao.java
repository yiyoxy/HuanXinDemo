package com.tron.huanxindemo.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tron.huanxindemo.model.bean.GroupInfo;
import com.tron.huanxindemo.model.bean.InvitationInfo;
import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.model.db.DBHelper;
import com.tron.huanxindemo.model.table.InvitationTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tron on 2017/2/16.
 * <p>
 * 邀请信息表的操作类
 */

public class InvitationTableDao {

    private DBHelper dbHelper;

    public InvitationTableDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // 添加邀请到邀请信息表中
    public void addInvitation(InvitationInfo invitationInfo) {

        // 校验
        if (invitationInfo == null) {
            return;
        }

        // 获取连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();

        UserInfo userInfo = invitationInfo.getUserInfo();
        if (userInfo == null) {
            // 群组邀请的信息: 群组名称, 群组id, 邀请人的环信id
            contentValues.put(InvitationTable.COL_GROUP_ID, invitationInfo.getGroupInfo().getGroupId());
            contentValues.put(InvitationTable.COL_GROUP_NAME, invitationInfo.getGroupInfo().getGroupName());
            contentValues.put(InvitationTable.COL_USER_HXID, invitationInfo.getGroupInfo().getInvitePerson());
        } else {
            // 联系人邀请:邀请人的环信id, 邀请人名称
            contentValues.put(InvitationTable.COL_USER_HXID, invitationInfo.getUserInfo().getHxid());
            contentValues.put(InvitationTable.COL_USER_NAME, invitationInfo.getUserInfo().getUsername());
        }

        contentValues.put(InvitationTable.COL_REASON, invitationInfo.getReason());
        // Enum还有一个ordinal的方法，这个方法返回枚举值在枚举类种的顺序，这个顺序根据枚举值声明的顺序而定.
        contentValues.put(InvitationTable.COL_STATUS, invitationInfo.getStatus().ordinal());

        database.replace(InvitationTable.TABLE_NAME, null, contentValues);

    }

    // 从表中获取所有邀请信息
    public List<InvitationInfo> getInvitations() {

        // 获取数据库连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String sql = "select * from " + InvitationTable.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);

        List<InvitationInfo> invitationInfos = new ArrayList<>();

        while (cursor.moveToNext()) {
            InvitationInfo invitationInfo = new InvitationInfo();

            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_REASON)));
            invitationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_STATUS))));

            String groupId = cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_ID));

            if (groupId == null) {
                // 联系人
                UserInfo userInfo = new UserInfo();

                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_HXID)));
                userInfo.setUsername(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_NAME)));

                invitationInfo.setUserInfo(userInfo);
            } else {
                // 群组
                GroupInfo groupInfo = new GroupInfo();

                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_ID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_NAME)));
                groupInfo.setInvitePerson(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_HXID)));

                invitationInfo.setGroupInfo(groupInfo);
            }

            invitationInfos.add(invitationInfo);

        }

        cursor.close();

        return invitationInfos;
    }

    // 将int类型状态转换为邀请状态
    private InvitationInfo.InvitationStatus int2InviteStatus(int intStatus) {

        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }

        return null;
    }

    // 删除邀请
    public void removeInvitation(String hxId) {
        // 校验
        if (hxId == null) {
            return;
        }

        // 获取数据库连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        database.delete(InvitationTable.TABLE_NAME, InvitationTable.COL_USER_HXID + "=?", new String[]{hxId});

    }

    // 更新邀请状态
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus, String hxId) {
        if (TextUtils.isEmpty(hxId)) {
            return;
        }

        // 获取数据库连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(InvitationTable.COL_STATUS, invitationStatus.ordinal());

        // 参数: 1.表名; 2.修改的字段和值; 3.条件选择; 4.条件选择的值
        database.update(InvitationTable.TABLE_NAME, contentValues,
                InvitationTable.COL_USER_HXID + "=?", new String[]{hxId});
    }

}
