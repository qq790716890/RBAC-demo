package com.rbac_demo.controller;


import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.R;

import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @author : lzy
 * @date : 2023/6/9
 * @effect : 用户登陆相关api
 */


@RestController
@CrossOrigin("*")
@Slf4j
public class LoginController {

    @Autowired
    private EmployeeService employeeService;



    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee emp){
        Employee findEmp = employeeService.findEmployeeByUserName(emp.getUserName());
        if (findEmp == null || !findEmp.getPassword().equals(emp.getPassword())){
            return R.error("用户或密码错误！");
        }
        if (findEmp.getStatus()==0){
            return R.error("用户已被禁用，请联系管理员！！！");
        }
        employeeService.fillEmpInfo(findEmp);
        EmployeeContext.setEmployee(findEmp);
        return R.success(findEmp);
    }

    @PostMapping("/logout")
    public R<String> logout(@RequestBody Employee emp){
        EmployeeContext.clear();
        return R.success("退出登陆成功!");
    }
}
