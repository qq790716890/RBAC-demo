package com.rbac_demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/10
 * @effect : 用户实体
 */

@Data
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

}
