package com.tron.huanxindemo.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tron.huanxindemo.model.table.AccountTable;

/**
 * Created by Tron on 2017/2/15.
 *
 * 创建用户账号数据库
 */

public class AccountDb extends SQLiteOpenHelper {

    // 版本
    private static final int VERSION = 1;
    // 数据库名
    private static final String DB_NAME = "account.db";

    // 创建数据库
    public AccountDb(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // 数据库第一次被创建时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(AccountTable.CREATE_TABLE);
    }

    // 版本升级时被调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 更新数据库

    }
}
