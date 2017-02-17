package com.tron.huanxindemo.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.adapter.InviteMessageAdapter;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.model.bean.InvitationInfo;
import com.tron.huanxindemo.utils.Constant;
import com.tron.huanxindemo.utils.ShowToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteMessageActivity extends AppCompatActivity {

    @BindView(R.id.invite_msg_lv)
    ListView inviteMsgLv;

    private InviteMessageAdapter adapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_message);
        ButterKnife.bind(this);

        initView();

        initData();
    }

    private void initData() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(receiver, new IntentFilter(Constant.NEW_INVITE_CHANGE));
    }

    private void initView() {

        adapter = new InviteMessageAdapter(this, new InviteMessageAdapter.OnInviteChangeListener() {
            // 接受
            @Override
            public void onAccept(final InvitationInfo info) {
                Model.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        // 网络: 通知环信服务器
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(
                                    info.getUserInfo().getHxid());

                            // 本地
                            Model.getInstance().getDBManager().getInvitationTableDao().updateInvitationStatus(
                                    InvitationInfo.InvitationStatus.INVITE_ACCEPT, info.getUserInfo().getHxid());

                            // 内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 刷新界面
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this, "接受成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.show(InviteMessageActivity.this, "接受失败" + e.getMessage());
                        }
                    }
                });
            }

            // 拒绝
            @Override
            public void onReject(final InvitationInfo info) {

                Model.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        // 网络:通知环信服务器
                        try {
                            EMClient.getInstance().contactManager().declineInvitation(
                                    info.getUserInfo().getHxid());

                            // 本地
                            Model.getInstance().getDBManager().getInvitationTableDao()
                                    .removeInvitation(info.getUserInfo().getHxid());
                            Model.getInstance().getDBManager().getContactTableDao()
                                    .deleteContactByHxId(info.getUserInfo().getHxid());

                            // 内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 刷新界面
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this, "拒绝成功");
                                }
                            });

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.show(InviteMessageActivity.this, "拒绝失败" + e.getMessage());
                        }
                    }
                });
            }
        });

        inviteMsgLv.setAdapter(adapter);

        refresh();
    }

    private void refresh() {

        // 获取数据
        List<InvitationInfo> invitations = Model.getInstance().getDBManager().getInvitationTableDao().getInvitations();

        // 刷新数据
        if (invitations == null){
            return;
        }

        // 刷新
        adapter.refresh(invitations);
    }
}
