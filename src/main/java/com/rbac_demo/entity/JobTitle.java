package com.rbac_demo.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/11
 * @effect :
 */
@Data
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

}
