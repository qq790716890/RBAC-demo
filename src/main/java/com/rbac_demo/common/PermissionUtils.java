package com.rbac_demo.common;

import com.rbac_demo.annotation.Logical;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */
public class PermissionUtils implements ConstantUtils {

    private PermissionUtils() {
    }

    private static final Logger log = LoggerFactory.getLogger(PermissionUtils.class);

    public static String getAllJobTitlePermissions() {
        String[] ls = new String[]{JOBTITLE_READ, JOBTITLE_UPDATE, JOBTITLE_INSERT, JOBTITLE_DELETE};
        return String.join(",", ls);
    }

    public static String getAllEmployeePermissions() {
        String[] ls = new String[]{EMP_READ, EMP_UPDATE, EMP_INSERT, EMP_DELETE};
        return String.join(",", ls);
    }

    public static String getAllDepartmentPermissions() {
        String[] ls = new String[]{DEP_READ, DEP_UPDATE, DEP_INSERT, DEP_DELETE};
        return String.join(",", ls);
    }

    public static String[] string2Arr(String permission) {
        return permission.split(",");
    }


    public static boolean permissionsCheck(String[] required, String[] empHas, Logical logical) {
        if (logical == Logical.AND) {
            // 有一个权限没有，就返回false
            return ifHasAll(required, empHas);
        } else {
            return ifHasAny(required, empHas);
        }
    }

    private static boolean ifHasAny(String[] required, String[] empHas) {
        // 有任何一个权限，就返回true
        for (String re : required) {
            for (String hs : empHas) {
                if (re.equals(hs)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean ifHasAll(String[] required, String[] empHas) {
        for (String re : required) {
            boolean find = false;
            for (String hs : empHas) {
                if (re.equals(hs)) {
                    find = true;
                    break;
                }
            }
            if (!find) return false;
        }
        return true;
    }


    public static boolean checkEmpRank(Employee employee, Employee employee1) {
        // 1. 用户的 部门权限 大于 要操作的对象的部门权限 或者 两者同一部门
        // 2. 用户的 职位权限 大于或者等于 要操作的对象的职位权限
        // ********* TIP: Employee employee, Employee employee1，注意
        // ********* TIP: 数据库中没有Employee 没有 depRank 和 jobRank的信息，请务必这里查到了这两个的信息
        if (employee.getDepRank() == null || employee.getJobRank() == null || employee1.getDepRank() == null || employee1.getJobRank() == null) {
            log.warn("请确认比对的两个Employee 都注入了JOB RANK 和 DEP RANK !!!!!!");
            return false;
        }
        return (intComp(employee.getDepRank(), employee1.getDepRank(), CMP.LESS) || Objects.equals(employee.getDepartmentId(), employee1.getDepartmentId()) && checkJobTitleRank(employee.getJobRank(), employee1.getJobRank()));

    }

    public static boolean checkJobTitleRank(int empRank, int jobRank) {
        return intComp(empRank, jobRank, CMP.LESS_EQ);
    }

    public static boolean checkDepartmentRank(Employee employee, Department dep) {
        return intComp(employee.getDepRank(), dep.getRank(), CMP.LESS) || Objects.equals(employee.getDepartmentId(), dep.getId());
    }

    public static boolean intComp(int userHas, int toCheck, CMP cmpType) {
        switch (cmpType) {
            case LESS:
                return userHas < toCheck;
            case LESS_EQ:
                return userHas <= toCheck;
            case EQ:
                return userHas == toCheck;
            case MORE:
                return userHas > toCheck;
            case MORE_EQ:
                return userHas >= toCheck;
            default:
                return true;
        }
    }

}
