package com.tron.huanxindemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.adapter.GroupListAdapter;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.utils.ShowToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupListActivity extends AppCompatActivity {

    @BindView(R.id.lv_group_list)
    ListView lvGroupList;

    private LinearLayout groupListHead;

    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ButterKnife.bind(this);

        initView();

        initData();

        initListener();
    }

    private void initView() {
        // 添加头布局
        View headView = View.inflate(this, R.layout.group_list_head, null);

        // 初始化控件
        groupListHead = (LinearLayout) headView.findViewById(R.id.ll_group_list);

        // ListView添加头布局
        lvGroupList.addHeaderView(groupListHead);

        // 创建适配器
        groupListAdapter = new GroupListAdapter(this);

        // 将适配器添加到listview
        lvGroupList.setAdapter(groupListAdapter);

    }

    private void initData() {
        Model.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // 1.网络:从环信服务器获取群组联系人信息, 并自动存一份到本地
                    List<EMGroup> groups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    // 2.内存和页面, 此处在主线程中执行
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                            ShowToast.show(GroupListActivity.this,"加载群页面成功");
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ShowToast.showUI(GroupListActivity.this,"服务器获取数据失败" + e.getMessage());
                }
            }
        });

    }

    // ListView刷新页面
    private void refresh() {

        // 从本地获取
        List<EMGroup> allGroups = EMClient.getInstance().groupManager().getAllGroups();

        groupListAdapter.refresh(allGroups);
    }

    // 创建监听
    private void initListener() {

        // 创建群组监听
        groupListHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到群组创建的界面
                Intent intent = new Intent(GroupListActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        // item点击事件监听
        lvGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    return;
                }

                // 跳转到聊天界面
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);

                EMGroup emGroup = EMClient.getInstance().groupManager().getAllGroups().get(position - 1);

                intent.putExtra(EaseConstant.EXTRA_USER_ID, emGroup.getGroupId());

                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 刷新界面
        refresh();
    }
}
