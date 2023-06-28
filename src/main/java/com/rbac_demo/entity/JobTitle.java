package com.rbac_demo.entity;


import java.util.Date;

/**
 * @author : lzy
 * @date : 2023/6/11
 * @effect :
 */
public class JobTitle {

    private Integer id;

    private String name;
    private Integer rank;    // 职位级别
    private String description; // 部门描述
    private String permissions; // 职位权限, 字符串，逗号分割

    private Date createTime;
    private Long createUserId;

    private Date updateTime;
    private Long updateUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Override
    public String toString() {
        return "JobTitle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rank=" + rank +
                ", description='" + description + '\'' +
                ", permissions='" + permissions + '\'' +
                ", createTime=" + createTime +
                ", createUserId=" + createUserId +
                ", updateTime=" + updateTime +
                ", updateUserId=" + updateUserId +
                '}';
    }
}
