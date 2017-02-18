package com.tron.huanxindemo.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.tron.huanxindemo.R;
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

    // 接收广播信息来设置小红点的隐藏或显示
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isShowPoint();
        }
    };

    private LocalBroadcastManager broadcastManager;

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

        // 添加头布局
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

        // 从环信服务器获取联系人信息
        initData();
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

                    for (int i = 0; i < contacts.size(); i++){
                        userInfos.add(new UserInfo(contacts.get(i)));
                    }

                    Model.getInstance().getDBManager().getContactTableDao().savaContacts(userInfos, true);

                    // 内存和网页
                    refreshContacts();

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
        List<UserInfo> userInfos = Model.getInstance().getDBManager().getContactTableDao().getContacts();

        // 校验
        if (userInfos == null){
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
                ShowToast.show(getActivity(), "群组");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        broadcastManager.unregisterReceiver(receiver);
    }
}
