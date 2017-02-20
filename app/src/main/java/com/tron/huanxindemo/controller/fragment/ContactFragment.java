package com.tron.huanxindemo.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.activity.ChatActivity;
import com.tron.huanxindemo.controller.activity.GroupListActivity;
import com.tron.huanxindemo.controller.activity.InviteActivity;
import com.tron.huanxindemo.controller.activity.InviteMessageActivity;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.utils.Constant;
import com.tron.huanxindemo.utils.ShowToast;
import com.tron.huanxindemo.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Tron on 2017/2/15.
 */

public class ContactFragment extends EaseContactListFragment {

    @BindView(R.id.contact_iv_invite)
    ImageView contactIvInvite;
    @BindView(R.id.ll_new_friends)
    LinearLayout llNewFriends;
    @BindView(R.id.ll_groups)
    LinearLayout llGroups;
    private Unbinder unbinder;

    private LocalBroadcastManager broadcastManager;

    // 接收广播信息来设置小红点的隐藏或显示
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isShowPoint();
        }
    };

    private List<UserInfo> userInfos;

    // 联系人状态改变的广播
    private BroadcastReceiver contactReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshContacts();
        }
    };

    // 接收群组状态改变的广播
    private BroadcastReceiver groupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isShowPoint();
        }
    };

    // 是否显示小红点
    public void isShowPoint() {
        boolean isShow = SpUtils.getInstance().getBoolean(SpUtils.NEW_INVITE, false);
        contactIvInvite.setVisibility(isShow ? View.VISIBLE : View.GONE);

        Log.e("TAG", "isShow=====" + isShow);
    }

    @Override
    protected void initView() {
        super.initView();

        // 初始化头布局
        View view = View.inflate(getActivity(), R.layout.fragment_contact_head, null);
        // ButterKnife注入view
        unbinder = ButterKnife.bind(this, view);

        // 添加头布局; 如果listview中添加了header则position=0代表header
        listView.addHeaderView(view);

        // 添加actionbar右侧的加号, 设置监听
        titleBar.setRightImageResource(R.mipmap.em_add);
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到邀请界面
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });

        // 初始化小红点
        isShowPoint();

        // 注册广播
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastManager.registerReceiver(receiver, new IntentFilter(Constant.NEW_INVITE_CHANGE));

        // 联系人更新的广播
        broadcastManager.registerReceiver(contactReceiver, new IntentFilter(Constant.CONTACT_CHANGE));

        // 注册群组状态更新的广播
        broadcastManager.registerReceiver(groupReceiver, new IntentFilter(Constant.GROUP_INVITE_CHANGE));

        // 初始化数据:从环信服务器获取联系人信息
        initData();

        // 监听事件
        initListener();
    }

    private void initListener() {

        // 联系人点击监听
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {

                // 跳转到会话界面
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                // 传参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());

                startActivity(intent);
            }
        });

        // 设置listview的长按监听
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // position==0是添加的头部
                if (position == 0){
                    return false;
                }

                showDialog(position);
                return true;
            }
        });
    }

    private void showDialog(final int position) {
        new AlertDialog.Builder(getActivity())
                .setMessage("你确定要删除此联系人吗?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteContact(position);
                    }
                })
                .create()
                .show();
    }

    // 删除联系人: 先从网络删除, 再在本地删除
    private void deleteContact(final int position) {
        Model.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1.网络删除
                    // 获取到这个用户的环信id
                    UserInfo userInfo = userInfos.get(position - 1);

                    // 网络删除
                    EMClient.getInstance().contactManager().deleteContact(userInfo.getHxid());

                    // 2.本地删联系人和邀请信息
                    Model.getInstance().getDBManager().getContactTableDao().deleteContactByHxId(userInfo.getHxid());
                    Model.getInstance().getDBManager().getInvitationTableDao().removeInvitation(userInfo.getHxid());

                    // 3.刷新
                    refreshContacts();
                    ShowToast.showUI(getActivity(), "删除成功");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ShowToast.showUI(getActivity(), "删除失败" + e.getMessage());
                }
            }
        });
    }

    private void initData() {

        // 获取联系人
        Model.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1.服务器获取 ; 返回联系人的集合{username1, username2, username3, ...}
                    List<String> contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    // 保存到数据库
                    List<UserInfo> userInfos = new ArrayList<>();

                    for (int i = 0; i < contacts.size(); i++) {
                        userInfos.add(new UserInfo(contacts.get(i)));
                    }

                    Model.getInstance().getDBManager().getContactTableDao().savaContacts(userInfos, true);

                    // 内存和网页
                    if (getActivity() == null){
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshContacts();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ShowToast.showUI(getActivity(), "获取联系人失败" + e.getMessage());
                }
            }
        });

    }

    // 刷新联系人
    private void refreshContacts() {

        // 从本地获取数据
        userInfos = Model.getInstance().getDBManager().getContactTableDao().getContacts();

        // 校验
        if (userInfos == null) {
            return;
        }

        // 转换数据
        HashMap<String, EaseUser> maps = new HashMap<>();

        for (UserInfo userInfo : userInfos) {
            EaseUser user = new EaseUser(userInfo.getHxid());
            maps.put(userInfo.getHxid(), user);
        }

        setContactsMap(maps);

        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 刷新联系人界面
        refreshContacts();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    @OnClick({R.id.ll_new_friends, R.id.ll_groups})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new_friends:
                // 隐藏小红点
                SpUtils.getInstance().save(SpUtils.NEW_INVITE, false);
                isShowPoint();

                // 跳转
                Intent intent = new Intent(getActivity(), InviteMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_groups:
                // 点击跳转到群列表界面
                intent = new Intent(getActivity(), GroupListActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        // 解注册广播监听
        broadcastManager.unregisterReceiver(receiver);
        broadcastManager.unregisterReceiver(contactReceiver);
        broadcastManager.unregisterReceiver(groupReceiver);
    }
}
