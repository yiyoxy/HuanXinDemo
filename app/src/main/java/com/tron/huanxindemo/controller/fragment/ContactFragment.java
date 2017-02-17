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

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.activity.InviteActivity;
import com.tron.huanxindemo.controller.activity.InviteMessageActivity;
import com.tron.huanxindemo.utils.Constant;
import com.tron.huanxindemo.utils.ShowToast;
import com.tron.huanxindemo.utils.SpUtils;

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
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastManager.registerReceiver(receiver, new IntentFilter(Constant.NEW_INVITE_CHANGE));

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
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
