package com.tron.huanxindemo.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.fragment.ContactFragment;
import com.tron.huanxindemo.controller.fragment.ConversationFragment;
import com.tron.huanxindemo.controller.fragment.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;

    private SettingsFragment settingsFragment;
    private ConversationFragment conversationFragment;
    private ContactFragment contactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 初始化数据
        initData();

        // 设置监听
        initListener();
    }

    private void initData() {
        // 创建Fragment
        settingsFragment = new SettingsFragment();
        conversationFragment = new ConversationFragment();
        contactFragment = new ContactFragment();

        // 默认显示会话页面
        switchFragment(R.id.rb_main_conversation);

    }

    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 切换Fragment
                switchFragment(checkedId);
            }
        });
    }

    private void switchFragment(int checkedId) {
        Fragment fragment = null;
        switch (checkedId) {
            case R.id.rb_main_contact:
                fragment = contactFragment;
                break;
            case R.id.rb_main_conversation:
                fragment = conversationFragment;
                break;
            case R.id.rb_main_setting:
                fragment = settingsFragment;
                break;
        }
        if (fragment == null) {
            return;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, fragment).commit();
    }
}
