package com.tron.huanxindemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.tron.huanxindemo.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.bt_newgroup_create)
    public void onClick() {
        if (validate()){
            // 跳转到联系人页面获取联系人数据
            Intent intent = new Intent(CreateGroupActivity.this, PickContactActivity.class);
            startActivity(intent);
        }
    }

    private boolean validate() {

        String groupDesc = etNewgroupDesc.getText().toString().trim();
        String groupName = etNewgroupName.getText().toString().trim();

        if (TextUtils.isEmpty(groupDesc)) {
            ShowToast.show(this, "群名称不能为空");
            return false;
        }

        if (TextUtils.isEmpty(groupName)){
            ShowToast.show(this, "群简介不能为空");
            return false;
        }

        return true;
    }
}
