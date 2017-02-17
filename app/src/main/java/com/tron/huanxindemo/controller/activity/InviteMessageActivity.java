package com.tron.huanxindemo.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.tron.huanxindemo.R;
import com.tron.huanxindemo.controller.adapter.InviteMessageAdapter;
import com.tron.huanxindemo.model.Model;
import com.tron.huanxindemo.model.bean.InvitationInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteMessageActivity extends AppCompatActivity {

    @BindView(R.id.invite_msg_lv)
    ListView inviteMsgLv;

    private InviteMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_message);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        adapter = new InviteMessageAdapter(this);

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
