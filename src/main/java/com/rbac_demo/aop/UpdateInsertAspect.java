package com.rbac_demo.aop;

import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.JobTitle;
import com.rbac_demo.service.DepartmentService;
import com.rbac_demo.service.EmployeeService;
import com.rbac_demo.service.JobTitleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@Component
@Aspect
public class UpdateInsertAspect {
    // 更新操作的AOP
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JobTitleService jobTitleService;

    @Autowired
    private DepartmentService departmentService;

    @Before("execution(* com.rbac_demo.service.*.update*(..)) && args(obj)")
    public void updateAspect(JoinPoint joinPoint, Object obj) {
        // 在这里进行参数修改 更新时间等
        // 这里这样写比较麻烦，还可以设置一个  UpdateTimeAware 接口，让这三个类实现这个接口设置值，
        // 然后传入的这里obj 就可以设置成 UpdateTimeAware对象，进行调用方法进行设置
        Long currentUserId = EmployeeContext.getEmployee().getId();
        if (obj instanceof Employee) {
            Employee employee = (Employee) obj;

            // 查询原对象时间
            Date preDate = employeeService.selectOneById(employee.getId()).getUpdateTime();
            // 更新的期间，已经被人修改了。
            if (preDate !=null && !preDate.equals(employee.getUpdateTime())){
                throw new CustomException("当前内容已经被更新，请您刷新后重试！");
            }

            employee.setUpdateTime(new Date());
            employee.setUpdateUserId(currentUserId);

        } else if (obj instanceof Department) {
            Department department = (Department) obj;

            Date preDate = departmentService.selectOneById(department.getId()).getUpdateTime();
            // 更新的期间，已经被人修改了。
            if (preDate !=null &&  !preDate.equals(department.getUpdateTime())){
                throw new CustomException("当前内容已经被更新，请您刷新后重试！");
            }

            department.setUpdateTime(new Date());
            department.setUpdateUserId(currentUserId);

        } else if (obj instanceof JobTitle) {
            JobTitle jobTitle = (JobTitle) obj;

            Date preDate = departmentService.selectOneById(jobTitle.getId()).getUpdateTime();
            // 更新的期间，已经被人修改了。
            if (preDate !=null && !preDate.equals(jobTitle.getUpdateTime())){
                throw new CustomException("当前内容已经被更新，请您刷新后重试！");
            }

            jobTitle.setUpdateTime(new Date());
            jobTitle.setUpdateUserId(currentUserId);
        }
        // 可以根据需要继续处理其他类型的参数
    }

    // 创建操作的AOP
    @Before("execution(* com.rbac_demo.service.*.insert*(..)) && args(obj)")
    public void createAspect(JoinPoint joinPoint, Object obj) {

        Long currentUserId = EmployeeContext.getEmployee().getId();
        if (obj instanceof Employee) {
            Employee employee = (Employee) obj;
            employee.setCreateTime(new Date());
            employee.setCreateUserId(currentUserId);

        } else if (obj instanceof Department) {
            Department department = (Department) obj;
            department.setCreateTime(new Date());
            department.setCreateUserId(currentUserId);

        } else if (obj instanceof JobTitle) {
            JobTitle jobTitle = (JobTitle) obj;
            jobTitle.setCreateTime(new Date());
            jobTitle.setCreateUserId(currentUserId);

        }
    }

}
