package com.tron.huanxindemo.controller.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
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

    // 头部点击监听
    private void initListener() {
        groupListHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
