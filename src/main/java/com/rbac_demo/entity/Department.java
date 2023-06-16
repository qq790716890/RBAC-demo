package com.rbac_demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : lzy
 * @date : 2023/6/11
 * @effect :
 */

@Data
public class Department implements Serializable {

    private Integer id;

    private String name;
    private Integer rank;    // 部门级别
    private String description; // 部门描述

    private Date createTime;
    private Long createUserId;

    private Date updateTime;
    private Long updateUserId;


}
