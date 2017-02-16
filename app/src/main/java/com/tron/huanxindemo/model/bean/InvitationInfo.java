package com.tron.huanxindemo.model.bean;

/**
 * Created by Tron on 2017/2/16.
 * <p>
 * 邀请信息数据bean类
 */

public class InvitationInfo {

    // 好友
    private UserInfo userInfo;
    // 群组
    private GroupInfo groupInfo;
    // 邀请
    private String reason;
    // 状态
    private InvitationStatus status;

    public InvitationInfo() {
    }

    public InvitationInfo(UserInfo userInfo, GroupInfo groupInfo, String reason, InvitationStatus status) {
        this.userInfo = userInfo;
        this.groupInfo = groupInfo;
        this.reason = reason;
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
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

    /**
     * 枚举其实就是一种类型，跟int, char这种差不多，就是定义变量时限制输入的，你只能够赋enum里面规定的值
     * 枚举是一种类型，用于定义变量，以限制变量的赋值 赋值时通过"枚举名.值"来取得相关枚举中的值
     *
     * 在JDK1.5之前，我们定义常量都是： public static final ...
     * 现在好了，有了枚举，可以把相关的常量分组到一个枚举类型里，而且枚举提供了比常量更多的方法。
     */
    // 邀请信息状态
    public enum InvitationStatus {

        //联系人邀请信息状态
        // 新邀请
        NEW_INVITE,

        //接受邀请
        INVITE_ACCEPT,

        // 邀请被接受
        INVITE_ACCEPT_BY_PEER,


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
