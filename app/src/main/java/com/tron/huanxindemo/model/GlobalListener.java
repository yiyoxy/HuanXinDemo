package com.tron.huanxindemo.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.tron.huanxindemo.model.bean.GroupInfo;
import com.tron.huanxindemo.model.bean.InvitationInfo;
import com.tron.huanxindemo.model.bean.UserInfo;
import com.tron.huanxindemo.utils.Constant;
import com.tron.huanxindemo.utils.SpUtils;

/**
 * Created by Tron on 2017/2/16.
 * <p>
 * 全局监听联系人变化
 */
public class GlobalListener {

    /*
    LocalBroadcastManager是Android Support包提供的一个工具，
    是用来在同一个应用内的不同组件间发送Broadcast的。

    使用LocalBroadcastManager有如下好处：

    发送的广播只会在自己App内传播，不会泄露给其他App，确保隐私数据不会泄露
    其他App也无法向你的App发送该广播，不用担心其他App会来搞破坏,比系统全局广播更加高效
    */
    private final LocalBroadcastManager broadcastManager;

    public GlobalListener(Context context) {

        // 注册联系人监听
        EMClient.getInstance().contactManager().setContactListener(contactListener);

        // 注册群监听
        EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);

        // 本地广播
        broadcastManager = LocalBroadcastManager.getInstance(context);
    }

    EMContactListener contactListener = new EMContactListener() {

        // 收到好友邀请, 别人加你
        @Override
        public void onContactInvited(String username, String reason) {

            // 加到邀请信息表
            InvitationInfo invitationInfo = new InvitationInfo();

            invitationInfo.setUserInfo(new UserInfo(username));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);

            Model.getInstance().getDBManager().getInvitationTableDao().addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);

            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.NEW_INVITE_CHANGE));
        }

        // 好友请求被同意, 你加别人的时候,别人同意了
        @Override
        public void onContactAgreed(String username) {

            // 添加到邀请信息表
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUserInfo(new UserInfo(username));
            invitationInfo.setReason("邀请被接受");
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);

            Model.getInstance().getDBManager().getInvitationTableDao().addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);

            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.NEW_INVITE_CHANGE));
        }

        // 好友请求被拒绝, 你加别人, 但别人拒绝了
        @Override
        public void onContactRefused(String username) {

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);

            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.NEW_INVITE_CHANGE));
        }

        // 被删除时回调此方法, 别人把你删除了
        @Override
        public void onContactDeleted(String username) {

            // 删除邀请信息
            Model.getInstance().getDBManager().getInvitationTableDao().removeInvitation(username);

            // 删除联系人
            Model.getInstance().getDBManager().getContactTableDao().deleteContactByHxId(username);

            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.CONTACT_CHANGE));
        }

        // 增加了联系人时回调此方法, 当你同意添加别人为好友
        @Override
        public void onContactAdded(String username) {

            // 保存联系人
            Model.getInstance().getDBManager().getContactTableDao().savaContact(new UserInfo(username), true);

            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.CONTACT_CHANGE));
        }
    };

    // 创建群监听
    private final EMGroupChangeListener groupListener = new EMGroupChangeListener() {

        // 收到别人邀请你加入群组的邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

            // 添加邀请到数据库
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_INVITE);
            invitationInfo.setGroupInfo(new GroupInfo(groupName, groupId, inviter));

            Model.getInstance().getDBManager().getInvitationTableDao()
                    .addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);
            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGE));
        }

        // 收到别人要加入你的群的申请
        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION);
            invitationInfo.setGroupInfo(new GroupInfo(groupName, groupId, reason));

            Model.getInstance().getDBManager().getInvitationTableDao()
                    .addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);
            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGE));
        }

        // 你的加群申请被同意
        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason("");
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
            invitationInfo.setGroupInfo(new GroupInfo(groupName, groupId, accepter));

            Model.getInstance().getDBManager().getInvitationTableDao()
                    .addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);
            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGE));
        }

        // 你的加群申请被拒绝
        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);
            invitationInfo.setGroupInfo(new GroupInfo(groupName, groupId, decliner));

            Model.getInstance().getDBManager().getInvitationTableDao()
                    .addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);
            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGE));
        }

        // 别人接收了你的群组邀请
        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            invitationInfo.setGroupInfo(new GroupInfo(groupId, groupId, reason));

            Model.getInstance().getDBManager().getInvitationTableDao()
                    .addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);
            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGE));
        }

        // 别人拒绝了你的群组邀请
        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED);
            invitationInfo.setGroupInfo(new GroupInfo(groupId, groupId, inviter));

            Model.getInstance().getDBManager().getInvitationTableDao()
                    .addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);
            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGE));

        }

        // 当前用户被管理员移除出群组
        @Override
        public void onUserRemoved(String s, String s1) {

        }

        // 群组被创建者解散
        @Override
        public void onGroupDestroyed(String s, String s1) {

        }

        // 自动接受群邀请
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String reason) {

            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            invitationInfo.setGroupInfo(new GroupInfo(groupId, groupId, inviter));

            Model.getInstance().getDBManager().getInvitationTableDao()
                    .addInvitation(invitationInfo);

            // 保存小红点的状态
            SpUtils.getInstance().save(SpUtils.NEW_INVITE, true);
            // 发送广播
            broadcastManager.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGE));
        }
    };

}
