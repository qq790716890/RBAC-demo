package com.rbac_demo.common;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

public interface ConstantUtils {

    int DEFAULT_EXPIRE_SECONDS = 24 * 3600 * 3; // 默认3天自动过期
    String EMP_READ = "EMPLOYEE_READ";
    String EMP_UPDATE = "EMPLOYEE_UPDATE";
    String EMP_INSERT = "EMPLOYEE_INSERT";
    String EMP_DELETE = "EMPLOYEE_DELETE";

    String JOBTITLE_READ   = "JOBTITLE_READ";
    String JOBTITLE_UPDATE = "JOBTITLE_UPDATE";
    String JOBTITLE_INSERT = "JOBTITLE_INSERT";
    String JOBTITLE_DELETE = "JOBTITLE_DELETE";

    String DEP_READ   = "DEPARTMENT_READ";
    String DEP_UPDATE = "DEPARTMENT_UPDATE";
    String DEP_INSERT = "DEPARTMENT_INSERT";
    String DEP_DELETE = "DEPARTMENT_DELETE";

    String RANK_CHECK = "RANK_CHECK";


    enum CMP{
        LESS,LESS_EQ,MORE,MORE_EQ,EQ
    }


}
