package com.tron.huanxindemo.model.bean;

/**
 * Created by Tron on 2017/2/16.
 *
 * 群组数据bean类
 */

public class GroupInfo {

    // 群组名称
    private String groupName;
    // 群组ID
    private String groupId;
    // 邀请人
    private String invitePerson;

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String groupId, String invitePerson) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.invitePerson = invitePerson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getInvitePerson() {
        return invitePerson;
    }

    public void setInvitePerson(String invitePerson) {
        this.invitePerson = invitePerson;
    }
}
