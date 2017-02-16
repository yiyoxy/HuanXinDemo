package com.tron.huanxindemo.model.db;

import android.content.Context;

import com.tron.huanxindemo.model.dao.ContactTableDao;
import com.tron.huanxindemo.model.dao.InvitationTableDao;

/**
 * Created by Tron on 2017/2/16.
 * <p>
 * 邀请信息和联系人数据库的管理类
 */

public class DBManager {

    private final ContactTableDao contactTableDao;
    private final InvitationTableDao invitationTableDao;
    private final DBHelper dbHelper;

    public DBManager(Context context, String name) {

        // 创建联系人和邀请信息数据库, 上下文, 数据库名
        dbHelper = new DBHelper(context, name);

        // 创建邀请信息表操作类
        invitationTableDao = new InvitationTableDao(dbHelper);

        // 创建联系人表操作类
        contactTableDao = new ContactTableDao(dbHelper);
    }

    public ContactTableDao getContactTableDao() {
        return contactTableDao;
    }

    public InvitationTableDao getInvitationTableDao() {
        return invitationTableDao;
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }
}
