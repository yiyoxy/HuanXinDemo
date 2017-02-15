package com.tron.huanxindemo;

import android.app.Application;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.tron.huanxindemo.model.Model;

/**
 * Created by Tron on 2017/2/15.
 */

public class IMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化环信 SDK
        initHXSdk();

        //初始化 Modle!!!!!!!!!!!!!!!!!!!
        Model.getInstance().init(this);
    }

    private void initHXSdk() {
        // 配置文件
        EMOptions options = new EMOptions();

        options.setAutoAcceptGroupInvitation(false);// 不自动接受群邀请信息
        options.setAcceptInvitationAlways(false);// 不总是一直接受所有邀请

        // 初始化EaseUI
        EaseUI.getInstance().init(this, options);

    }
}
