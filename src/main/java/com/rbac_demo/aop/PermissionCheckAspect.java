package com.rbac_demo.aop;


import com.rbac_demo.annotation.Logical;
import com.rbac_demo.annotation.RequiresPermissions;
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.PermissionUtils;

import com.rbac_demo.common.R;
import com.rbac_demo.controller.DepartmentController;
import com.rbac_demo.controller.EmployeeController;
import com.rbac_demo.controller.JobTitleController;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.JobTitle;
import com.rbac_demo.service.DepartmentService;
import com.rbac_demo.service.EmployeeService;
import com.rbac_demo.service.JobTitleService;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;

/**
 * @author : lzy
 * @date : 2023/6/15
 * @effect : AOP 检查接口访问权限
 */

@Component
@Aspect
@Slf4j
public class PermissionCheckAspect {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JobTitleService jobTitleService;

    @Autowired
    private DepartmentService departmentService;


    @Around("@annotation(com.rbac_demo.annotation.RequiresPermissions)")
    public Object permissionCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("开始校验[操作权限]");
        // 登录员工已有的权限
        Employee loginEmp = EmployeeContext.getEmployee();
        String[] hasPermissions = loginEmp.getPermissions();

        // 获取 annotation
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        RequiresPermissions annotation = methodSignature.getMethod().getAnnotation(RequiresPermissions.class);

        // 得到 annotation 中需要的 权限列表，以及 logical
        // 并同用户校验
        String[] requiredPerms = annotation.value();
        Logical logical = annotation.logical();
        log.debug("校验权限code: {}", Arrays.toString(requiredPerms));
        log.debug("用户已有权限: {}", Arrays.toString(hasPermissions));
        boolean pass = PermissionUtils.permissionsCheck(requiredPerms, hasPermissions, logical);
        if (!pass) {
            // 没有权限.
            log.debug("登录用户: [" + loginEmp.getUserName() + "] 没有访问权限！");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return R.error("权限不足");
        }

        // 检查是否需要 检查 rank ,需要看参数
        //  1.获取目标方法所属的Controller类
        Class<?> controllerClass = methodSignature.getMethod().getDeclaringClass();
        //  2.获取到到方法中的第一个参数
        Object[] args = joinPoint.getArgs();
        if (annotation.rankCheck()) {
            if (!rankCheck(loginEmp, args[0], controllerClass)) {
                // 没有权限.
                log.debug("登录用户: [" + loginEmp.getUserName() + "] 没有访问权限！");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return R.error("没有权限！");
            }
        }
        return joinPoint.proceed();
    }


    public boolean rankCheck(Employee employee, Object toCheck, Class<?> clazz) {
        // 根据参数对象，判断需要比对的 rank
        if (toCheck instanceof Employee) {
            Employee employee1 = (Employee) toCheck;
            employeeService.fillEmpInfo(employee1);
            return PermissionUtils.checkEmpRank(employee, employee1);

        } else if (toCheck instanceof Department) {
            Department dep = (Department) toCheck;
            return PermissionUtils.checkDepartmentRank(employee, dep);

        } else if (toCheck instanceof JobTitle) {
            JobTitle jobTitle = (JobTitle) toCheck;
            return PermissionUtils.checkJobTitleRank(employee.getJobRank(), jobTitle.getRank());

        } else if (toCheck instanceof Long || toCheck instanceof Integer) {
            // 根据controller方法，查询id对象，比较它们的rank
            long id = toCheck instanceof Long?(Long) toCheck:(long) (Integer) toCheck;;

            if (clazz != null) {
                if (DepartmentController.class.isAssignableFrom(clazz)) {
                    // DepartmentController 下的 id 参数
                    Department department = departmentService.selectOneById((int) id);
                    return PermissionUtils.checkDepartmentRank(employee, department);

                } else if (EmployeeController.class.isAssignableFrom(clazz)) {
                    // clazz不是 EmployeeController 类或其子类
                    // EmployeeController 下的 id 参数
                    Employee employee1 = employeeService.selectOneById(id);
                    employeeService.fillEmpInfo(employee1);
                    return PermissionUtils.checkEmpRank(employee, employee1);

                } else if (JobTitleController.class.isAssignableFrom(clazz)) {
                    // clazz不是 JobTitleController 类或其子类
                    // JobTitleController 下的 id 参数
                    JobTitle jobTitle = jobTitleService.selectOneById((int) id);
                    return PermissionUtils.checkJobTitleRank(employee.getJobRank(), jobTitle.getRank());
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException("要比对rank的对象不合法！！！");
        }
    }


}
