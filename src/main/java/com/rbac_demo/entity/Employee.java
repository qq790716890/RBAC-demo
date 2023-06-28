package com.rbac_demo.entity;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/10
 * @effect : 用户实体
 */


public class Employee implements Serializable {
    // 唯一userId
    private Long id;
    // 登陆账号 和 密码
    private String userName;
    private String password;

    private String name;
    private Integer status;     // 0 禁用，非0 激活的状态

    private Integer departmentId;
    private String departmentName;
    private Integer depRank;    // 部门级别

    private Integer jobTitleId;
    private String jobTitleName;
    private Integer jobRank;    // 职位级别

    private String[] permissions;   // 根据jobTitle拥有的权限

    private Date createTime;
    private Long createUserId;

    private Date updateTime;
    private Long updateUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepRank() {
        return depRank;
    }

    public void setDepRank(Integer depRank) {
        this.depRank = depRank;
    }

    public Integer getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Integer jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getJobTitleName() {
        return jobTitleName;
    }

    public void setJobTitleName(String jobTitleName) {
        this.jobTitleName = jobTitleName;
    }

    public Integer getJobRank() {
        return jobRank;
    }

    public void setJobRank(Integer jobRank) {
        this.jobRank = jobRank;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
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
        return "Employee{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", depRank=" + depRank +
                ", jobTitleId=" + jobTitleId +
                ", jobTitleName='" + jobTitleName + '\'' +
                ", jobRank=" + jobRank +
                ", permissions=" + Arrays.toString(permissions) +
                ", createTime=" + createTime +
                ", createUserId=" + createUserId +
                ", updateTime=" + updateTime +
                ", updateUserId=" + updateUserId +
                '}';
    }
}
