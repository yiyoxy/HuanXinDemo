package com.tron.huanxindemo.controller.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.activity.InviteActivity;
import com.tron.huanxindemo.utils.ShowToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Tron on 2017/2/15.
 */

public class ContactFragment extends EaseContactListFragment {

    @BindView(R.id.contanct_iv_invite)
    ImageView contanctIvInvite;
    @BindView(R.id.ll_new_friends)
    LinearLayout llNewFriends;
    @BindView(R.id.ll_groups)
    LinearLayout llGroups;
    private Unbinder unbinder;

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

    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    @OnClick({R.id.ll_new_friends, R.id.ll_groups})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new_friends:
                ShowToast.show(getActivity(), "好友邀请");
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
