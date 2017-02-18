package com.tron.huanxindemo.controller.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.tron.huanxindemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_fl)
    FrameLayout chatFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

        // 聊天的Fragment
        EaseChatFragment chatFragment = new EaseChatFragment();

        if (getIntent().getExtras() == null){
            return;
        }

        chatFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().replace(R.id.chat_fl, chatFragment).commit();
    }
}
