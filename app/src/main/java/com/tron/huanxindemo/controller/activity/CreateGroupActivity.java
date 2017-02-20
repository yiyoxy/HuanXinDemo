package com.tron.huanxindemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.tron.huanxindemo.R;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.utils.ShowToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGroupActivity extends AppCompatActivity {

    @BindView(R.id.et_newgroup_name)
    EditText etNewgroupName;
    @BindView(R.id.et_newgroup_desc)
    EditText etNewgroupDesc;
    @BindView(R.id.cb_newgroup_public)
    CheckBox cbNewgroupPublic;
    @BindView(R.id.cb_newgroup_invite)
    CheckBox cbNewgroupInvite;
    @BindView(R.id.bt_newgroup_create)
    Button btNewgroupCreate;

    // 群描述
    private String groupDesc;
    // 群名称
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.bt_newgroup_create)
    public void onClick() {
        if (validate()) {
            // 跳转到联系人页面获取联系人数据
            Intent intent = new Intent(CreateGroupActivity.this, PickContactActivity.class);

            // 回传数据
            startActivityForResult(intent, 1); // requestCode为1
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // 创建群组
            createGroup(data);
        }
    }

    private void createGroup(final Intent data) {
        Model.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                // 1.获得回调的数据: 群成员名称的数组
                String[] members = data.getStringArrayExtra("members");

                // 去环信服务器创建群
                // 2.获得群设置
                EMGroupManager.EMGroupOptions groupOptions = new EMGroupManager.EMGroupOptions();

                // 3.设置群最大成员数量
                groupOptions.maxUsers = 200;

                // 4.设置群类型
                if (cbNewgroupPublic.isChecked()) {
                    if (cbNewgroupInvite.isChecked()) {
                        // 公开群, 任何人都可以加入
                        groupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {
                        // 公开群, 需要群主同意才能加入(群主邀请或主动申请)
                        groupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                } else {
                    if (cbNewgroupInvite.isChecked()) {
                        // 私有群,群成员可以邀请人进群
                        groupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    } else {
                        // 私有群,只有群主可以邀请人
                        groupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;

                    }
                }

                // 5.创建群, 参数: 群名称, 群描述, 要邀请的群成员, 群的其他设置
                try {
                    EMClient.getInstance().groupManager()
                            .createGroup(groupName, groupDesc, members, "", groupOptions);

                    ShowToast.showUI(CreateGroupActivity.this, "创建群成功");

                    finish();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ShowToast.showUI(CreateGroupActivity.this, "创建群失败" + e.getMessage());
                }
            }
        });
    }

    // 验证, 获得群名称和群描述
    private boolean validate() {

        groupDesc = etNewgroupDesc.getText().toString().trim();
        groupName = etNewgroupName.getText().toString().trim();

        if (TextUtils.isEmpty(groupDesc)) {
            ShowToast.show(this, "群名称不能为空");
            return false;
        }

        if (TextUtils.isEmpty(groupName)) {
            ShowToast.show(this, "群简介不能为空");
            return false;
        }

        return true;
    }
}
