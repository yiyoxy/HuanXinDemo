package com.tron.huanxindemo.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tron.huanxindemo.model.table.ContactTable;
import com.tron.huanxindemo.model.table.InvitationTable;

/**
 * Created by Tron on 2017/2/16.
 * <p>
 * 创建联系人和邀请信息数据库
 */

public class DBHelper extends SQLiteOpenHelper {

    // 创建联系人数据库
    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    //  onCreate() : 数据库第一次创建时执行，只执行一次，以后不再执行
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建联系人表
        db.execSQL(ContactTable.CREATE_TABLE);
        // 创建邀请信息表
        db.execSQL(InvitationTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
