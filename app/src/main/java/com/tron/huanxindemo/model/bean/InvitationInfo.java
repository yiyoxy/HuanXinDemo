package com.tron.huanxindemo.model.bean;

/**
 * Created by Tron on 2017/2/16.
 *
 * 邀请信息数据bean类
 */

public class InvitationInfo {

    // 好友
    private String userInfo;
    // 群组
    private GroupInfo groupInfo;
    // 理由
    private String reason;
    // 状态
    private InvitationStatus status;

    public InvitationInfo() {
    }

    public InvitationInfo(String userInfo, GroupInfo groupInfo, String reason, InvitationStatus status) {
        this.userInfo = userInfo;
        this.groupInfo = groupInfo;
        this.reason = reason;
        this.status = status;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    // 邀请信息状态
    public enum InvitationStatus {

        // 联系人邀请信息状态
        NEW_INVITE,// 新邀请
        INVITE_ACCEPT,//接受邀请
        INVITE_ACCEPT_BY_PEER,// 邀请被接受

        // --以下是群组邀请信息状态--
        //收到邀请去加入群
        NEW_GROUP_INVITE,

        //收到申请群加入
        NEW_GROUP_APPLICATION,

        //群邀请已经被对方接受
        GROUP_INVITE_ACCEPTED,

        //群申请已经被批准
        GROUP_APPLICATION_ACCEPTED,

        //接受了群邀请
        GROUP_ACCEPT_INVITE,

        //批准的群加入申请
        GROUP_ACCEPT_APPLICATION,

        //拒绝了群邀请
        GROUP_REJECT_INVITE,

        //拒绝了群申请加入
        GROUP_REJECT_APPLICATION,

        //群邀请被对方拒绝
        GROUP_INVITE_DECLINED,

        //群申请被拒绝
        GROUP_APPLICATION_DECLINED
    }
}
