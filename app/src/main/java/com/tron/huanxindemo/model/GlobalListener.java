package com.tron.huanxindemo.model;

import android.content.Context;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

/**
 * Created by Tron on 2017/2/16.
 */
public class GlobalListener {

    public GlobalListener(Context context) {
        EMClient.getInstance().contactManager().setContactListener(listener);

    }

    EMContactListener listener = new EMContactListener() {

        //收到好友邀请
        @Override
        public void onContactAdded(String s) {

        }

        //好友请求被同意
        @Override
        public void onContactDeleted(String s) {

        }

        //被删除时回调此方法
        @Override
        public void onContactInvited(String s, String s1) {

        }

        //同意添加时回调此方法
        @Override
        public void onContactAgreed(String s) {

        }

        //好友请求被拒绝
        @Override
        public void onContactRefused(String s) {

        }
    };

}
