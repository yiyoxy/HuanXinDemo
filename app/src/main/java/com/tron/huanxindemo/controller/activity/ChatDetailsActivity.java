package com.tron.huanxindemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.tron.huanxindemo.IMApplication;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.utils.Constant;
import com.tron.huanxindemo.utils.ShowToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatDetailsActivity extends AppCompatActivity {

    @BindView(R.id.gv_group_detail)
    GridView gvGroupDetail;
    @BindView(R.id.bt_group_detail)
    Button btGroupDetail;
    // 群id
    private String groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

        // 获取群id
        groupid = getIntent().getStringExtra("groupid");

        if (TextUtils.isEmpty(groupid)) {
            return;
        }

        // 获取当前的群组
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupid);

        // 获取群主
        String owner = group.getOwner();

        if (EMClient.getInstance().getCurrentUser().equals(owner)) {
            // 是群主
            btGroupDetail.setText("解散群");

            btGroupDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Model.getInstance().getGlobalThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            // 网络: 去环信服务器解散群
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(groupid);

                                // 退群
                                exitGroup();

                                // 结束当前页面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        ShowToast.show(ChatDetailsActivity.this, "解散群成功");
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                ShowToast.showUI(ChatDetailsActivity.this, "解散群失败" + e.getMessage());
                            }
                        }
                    });
                }
            });
        } else {
            // 是成员
            btGroupDetail.setText("退群");

            btGroupDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Model.getInstance().getGlobalThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            // 网络: 告诉环信退群
                            try {
                                EMClient.getInstance().groupManager().leaveGroup(groupid);

                                exitGroup();

                                // 结束当前页面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        ShowToast.show(ChatDetailsActivity.this, "退群成功");
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                ShowToast.showUI(ChatDetailsActivity.this, "退群失败" + e.getMessage());
                            }
                        }
                    });
                }
            });
        }
    }

    // 退群
    private void exitGroup() {

        // 注意上下文
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(IMApplication.getContext());

        Intent intent = new Intent(Constant.DESTROY_GROUP);

        intent.putExtra("groupid", groupid);

        // 发送广播
        broadcastManager.sendBroadcast(intent);
    }

}
