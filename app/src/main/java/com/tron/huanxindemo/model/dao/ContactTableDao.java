package com.tron.huanxindemo.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.model.db.DBHelper;
import com.tron.huanxindemo.model.table.ContactTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tron on 2017/2/16.
 * <p>
 * 联系人信息表的操作类
 */

public class ContactTableDao {

    private DBHelper dbHelper;

    public ContactTableDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // 获取所有联系人
    public List<UserInfo> getContacts() {

        // 获取数据库连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // 查询
        String sql = "select * from " + ContactTable.TABLE_NAME
                + " where " + ContactTable.COL_IS_CONTACT + "=1";

        Cursor cursor = database.rawQuery(sql, null);

        // 保存数据的列表集合
        List<UserInfo> userInfos = new ArrayList<>();

        // 数据封装
        while (cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NAME)));
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_HXID)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NICK)));
            // 添加对象到集合中
            userInfos.add(userInfo);
        }
        // 关闭游标cursor
        cursor.close();

        return userInfos;
    }

    // 通过环信id获取单个联系人的信息
    public UserInfo getContactByHxId(String hxID) {

        // 校验
        if (TextUtils.isEmpty(hxID)) {
            return null;
        }

        // 获取数据库连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String sql = "select * from " + ContactTable.TABLE_NAME
                + " where " + ContactTable.COL_USER_HXID + "=?";

        Cursor cursor = database.rawQuery(sql, new String[]{hxID});

        UserInfo userInfo = null;
        if (cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NAME)));
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_HXID)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NICK)));
        }
        // 关闭游标
        cursor.close();
        return userInfo;
    }

    // 通过环信id获取用户联系人信息
    public List<UserInfo> getContactsByHxId(List<String> hxIds) {

        // 校验
        if (hxIds == null || hxIds.size() == 0) {
            return null;
        }

        // 封装数据
        List<UserInfo> userInfos = new ArrayList<>();
        for (String hxId : hxIds) {
            UserInfo userInfo = getContactByHxId(hxId);
            if (userInfo != null) {
                userInfos.add(userInfo);
            }
        }

        return userInfos;
    }

    // 保存单个联系人
    public void savaContact(UserInfo user, boolean isMyContact) {

        // 校验
        if (user == null) {
            return;
        }

        // 获取数据库连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactTable.COL_USER_HXID, user.getHxid());
        contentValues.put(ContactTable.COL_USER_NAME, user.getUsername());
        contentValues.put(ContactTable.COL_USER_NICK, user.getNick());
        contentValues.put(ContactTable.COL_USER_PHOTO, user.getPhoto());
        contentValues.put(ContactTable.COL_IS_CONTACT, isMyContact ? 1 : 0);

        database.replace(ContactTable.TABLE_NAME, null, contentValues);
    }

    // 保存联系人信息
    public void savaContacts(List<UserInfo> contacts, boolean isMyContact){

        // 判断
        if(contacts == null || contacts.size() == 0) {
            return;
        }

        // 保存
        for (UserInfo userInfo : contacts) {
            savaContact(userInfo, isMyContact);
        }
    }

    // 删除联系人信息
    public void deleteContactByHxId(String hxId){
        // 判断
        if(TextUtils.isEmpty(hxId)) {
            return;
        }

        // 获取数据库连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        database.delete(ContactTable.TABLE_NAME, ContactTable.COL_USER_HXID + "=?", new String[]{hxId});
    }

}
