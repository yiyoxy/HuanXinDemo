package com.tron.huanxindemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.tron.huanxindemo.IMApplication;

/**
 * 作者：Tronzzb on 2017/2/17 18:31.
 * 邮箱：278042465@qq.com
 */

public class SpUtils {

    public static final String NEW_INVITE = "new_invite";

    private static SpUtils instance = new SpUtils();

    private static SharedPreferences mSp = null;

    public static SpUtils getInstance() {

        if (mSp == null) {
            mSp = IMApplication.getContext().getSharedPreferences(EMClient.getInstance().getCurrentUser(), Context.MODE_PRIVATE);

            Log.e("TAG", "mSp=====" + mSp);
        }

        return instance;
    }

    // 保存
    public void save(String key, Object value) {
        if (value instanceof  String){
            mSp.edit().putString(key, (String) value).commit();
        } else if(value instanceof Boolean) {
            mSp.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer){
            mSp.edit().putInt(key, (Integer) value).commit();
        }
    }

    // 读取Strng类型数据
    public String getString(String key, String deValue){
        return mSp.getString(key, deValue);
    }

    // 读取boolean类型数据
    public boolean getBoolean(String key, boolean deValue){
        return mSp.getBoolean(key, deValue);
    }

    // 读取int类型数据
    public int getInt(String key, int deValue){
        return mSp.getInt(key, deValue);
    }

    public void destroy() {
        mSp = null;
    }
}
