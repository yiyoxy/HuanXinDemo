package com.tron.huanxindemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tron.huanxindemo.R;
import com.tron.huanxindemo.model.bean.GroupInfo;
import com.tron.huanxindemo.model.bean.InvitationInfo;
import com.tron.huanxindemo.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Tronzzb on 2017/2/17 21:56.
 * 邮箱：278042465@qq.com
 */

public class InviteMessageAdapter extends BaseAdapter {

    private Context context;

    private List<InvitationInfo> invitationInfos;

    public InviteMessageAdapter(Context context) {
        this.context = context;
        invitationInfos = new ArrayList<>();
    }

    public void refresh(List<InvitationInfo> invitationInfos){

        // 校验
        if (invitationInfos == null){
            return;
        }

        // 先把集合清除为空,再添加数据
        this.invitationInfos.clear();
        this.invitationInfos.addAll(invitationInfos);

        // 刷新界面
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return invitationInfos == null ? 0 : invitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return invitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 创建viewHolder
        ViewHolder viewHolder;

        if (convertView == null){
            // 创建convertView
            convertView = View.inflate(context, R.layout.adapter_message_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 绑定数据
        InvitationInfo invitationInfo =  invitationInfos.get(position);

        GroupInfo groupInfo = invitationInfo.getGroupInfo();

        if (groupInfo != null){
            // 群邀请

        } else {
            // 联系人邀请
            UserInfo userInfo = invitationInfo.getUserInfo();
            viewHolder.tvInviteName.setText(userInfo.getUsername());

            // 隐藏button
            viewHolder.btInviteAccept.setVisibility(View.GONE);
            viewHolder.btInviteReject.setVisibility(View.GONE);

            // 判断是否是新邀请
            if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE) {

                // 展示button
                viewHolder.btInviteReject.setVisibility(View.VISIBLE);
                viewHolder.btInviteAccept.setVisibility(View.VISIBLE);

                // 设置reason
                if (invitationInfo.getReason() == null){
                    viewHolder.tvInviteReason.setText("邀请好友");
                } else {
                    viewHolder.tvInviteReason.setText(invitationInfo.getReason());
                }
            } else if (invitationInfo.getStatus() ==  // 邀请被接受
                    InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER){
                if (invitationInfo.getReason() == null){
                    viewHolder.tvInviteReason.setText("邀请被接受");
                } else {
                    viewHolder.tvInviteReason.setText(invitationInfo.getReason());
                }
            } else if (invitationInfo.getStatus() ==  // 接收邀请
                    InvitationInfo.InvitationStatus.INVITE_ACCEPT){
                if (invitationInfo.getReason() == null) {
                    viewHolder.tvInviteReason.setText("接收邀请");
                } else {
                    viewHolder.tvInviteReason.setText(invitationInfo.getReason());
                }
            }
        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_invite_name)
        TextView tvInviteName;
        @BindView(R.id.tv_invite_reason)
        TextView tvInviteReason;
        @BindView(R.id.bt_invite_accept)
        Button btInviteAccept;
        @BindView(R.id.bt_invite_reject)
        Button btInviteReject;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
