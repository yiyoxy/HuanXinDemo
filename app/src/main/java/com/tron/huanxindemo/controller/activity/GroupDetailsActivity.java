package com.tron.huanxindemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.tron.huanxindemo.IMApplication;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.adapter.GroupDetailsAdapter;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.utils.Constant;
import com.tron.huanxindemo.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupDetailsActivity extends AppCompatActivity {

    @BindView(R.id.gv_group_detail)
    GridView gvGroupDetail;
    @BindView(R.id.bt_group_detail)
    Button btGroupDetail;

    private String groupid;

    private GroupDetailsAdapter mGroupDetailsAdapter;
    private EMGroup mGroup;
    private String mOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        ButterKnife.bind(this);

        // 获取群id
        getGroupId();

        // initView();

        // 初始化数据
        initData();

        // 获取群成员
        getGroupMembers();

        // 监听事件
        initListener();
    }

    private void initListener() {

        gvGroupDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        // 获取当前gridview适配器中的deletemode
                        boolean deleteMode = mGroupDetailsAdapter.getDeleteMode();

                        // 只有删除模式下才管用
                        if (deleteMode) {
                            mGroupDetailsAdapter.setDeleteMode(false);
                        }
                        break;
                }

                return false;
            }
        });
    }

    private void getGroupId() {

        // 获取群id
        groupid = getIntent().getStringExtra("groupid");

        if (TextUtils.isEmpty(groupid)) {
            return;
        }

    }

    private void initView() {

    }

    private void getGroupMembers() {

        Model.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                // 从网络获取群组
                try {
                    EMClient.getInstance().groupManager().getGroupFromServer(groupid);

                    // 获取群成员
                    List<String> members = mGroup.getMembers();

                    // 转类型
                    final List<UserInfo> userInfos = new ArrayList<>();

                    for (String hxid : members) {
                        userInfos.add(new UserInfo(hxid));
                    }

                    // 内存和网页
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGroupDetailsAdapter.refresh(userInfos);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {

        // 获取当前的群组
        mGroup = EMClient.getInstance().groupManager().getGroup(groupid);

        // 获取群主
        mOwner = mGroup.getOwner();

        if (EMClient.getInstance().getCurrentUser().equals(mOwner)) {
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
                                        ShowToast.show(GroupDetailsActivity.this, "解散群成功");
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                ShowToast.showUI(GroupDetailsActivity.this, "解散群失败" + e.getMessage());
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
                                        ShowToast.show(GroupDetailsActivity.this, "退群成功");
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                ShowToast.showUI(GroupDetailsActivity.this, "退群失败" + e.getMessage());
                            }
                        }
                    });
                }
            });
        }

        // 判断是否有邀请的权限
        boolean isCanModify = EMClient.getInstance().getCurrentUser().equals(mOwner) || mGroup.isPublic();
        // 初始化adapter
        mGroupDetailsAdapter = new GroupDetailsAdapter(this, isCanModify, mOwner, new GroupDetailsAdapter.OnMembersChangeListener() {

            @Override
            public void onRemoveGroupMember(UserInfo userInfo) {
                ShowToast.show(GroupDetailsActivity.this, "删除成功");
            }

            @Override
            public void onAddGroupMember(UserInfo userInfo) {
                ShowToast.show(GroupDetailsActivity.this, "添加成功");
            }
        });
        // 设置适配器
        gvGroupDetail.setAdapter(mGroupDetailsAdapter);
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
