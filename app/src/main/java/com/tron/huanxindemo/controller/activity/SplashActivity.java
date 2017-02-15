package com.tron.huanxindemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.chat.EMClient;
import com.tron.huanxindemo.MainActivity;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.model.Model;

public class SplashActivity extends AppCompatActivity {

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                // 进入主界面或登录界面
                enterMainOrLogin();
            }
        }
    };

    private void enterMainOrLogin() {
        Model.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器获取是否登录过
                boolean isLoggedBefore = EMClient.getInstance().isLoggedInBefore();

                if (isLoggedBefore) {
                    // 登录成功后的操作
                    Model.getInstance().loginSuccess(EMClient.getInstance().getCurrentUser());
                    // 登录过, 进入主界面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    // 结束当前页面
                    finish();
                } else {
                    // 没有登录过, 跳转到登陆界面
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    // 结束当前页面
                    finish();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 发送延迟消息
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 移除所有消息
        handler.removeCallbacksAndMessages(null);
    }
}
