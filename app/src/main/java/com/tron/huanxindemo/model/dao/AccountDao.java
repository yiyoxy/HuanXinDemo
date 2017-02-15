package com.tron.huanxindemo.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.model.db.AccountDb;
import com.tron.huanxindemo.model.table.AccountTable;

/**
 * Created by Tron on 2017/2/15.
 *
 * 对数据库的中的数据进行操作
 */

public class AccountDao {

    private final AccountDb accountDb;

    public AccountDao(Context context){
        // 创建AccountDb数据库对象
        accountDb = new AccountDb(context);
    }

    // 添加用户到数据库
    public void addAccount(UserInfo user){
        // 验证
        if(user == null) {
            return;
        }

        // 获取数据库连接
        SQLiteDatabase database = accountDb.getReadableDatabase();

        /**
         *  1.contenvalues只能存储基本类型的数据，像string，int之类的
         *  2.ContentValues存储对象的时候，以(key,value)的形式来存储数据
         *  3.在忘数据库中插入数据的时候，首先应该有一个ContentValues的对象所以
         */
        ContentValues contentValues = new ContentValues();

        contentValues.put(AccountTable.COL_USER_HXID, user.getHxid());  // column:列
        contentValues.put(AccountTable.COL_USER_NAME, user.getUsername());
        contentValues.put(AccountTable.COL_USER_NICK, user.getNick());
        contentValues.put(AccountTable.COL_USER_PHOTO, user.getPhoto());

        /**
         *  区别: update只会在原记录上更新字段的值，不会删除原有记录，然后再插入新纪录
         *
         *  1.如果要使用replace，一定要有一个primary key
         *  2.replace语句会删除原有的一条记录， 并且插入一条新的记录来替换原记录。
         *  3.一般用replace语句替换一条记录的所有列， 如果在replace语句中没有指定某列， 在replace之后这列的值被置空
         *  4.replace根据主键确定被替换的是哪一条记录
         *  5.replace语句不能根据where子句来定位要被替换的记录
         *  6.如果执行replace语句时， 不存在要替换的记录， 那么就会插入一条新的记录。
         *  7. 如果新插入的或替换的记录中， 有字段和表中的其他记录冲突， 那么会删除那条其他记录。
         *
         */
        database.replace(AccountTable.TABLE_NAME, null, contentValues);

    }

    // 根据环信id获取所有用户信息
    public UserInfo getAccountByHxid(String hxId){

        if(hxId == null || TextUtils.isEmpty(hxId)) {
            return null;
        }

        // 获取数据库连接
        SQLiteDatabase database = accountDb.getReadableDatabase();

        // select语句
        String sql = "selec * from" + AccountTable.COL_USER_NAME + " where"
                + AccountTable.COL_USER_HXID + "?=";

        // 第一个参数是sql语句, 第二个参数是sql语句的占位符(问号),对应的(参数)值
        Cursor cursor = database.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if(cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_HXID)));
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_PHOTO)));

        }

        // 关闭游标
        cursor.close();

        return userInfo;
    }

}
