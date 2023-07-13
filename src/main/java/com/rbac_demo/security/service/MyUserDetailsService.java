package com.rbac_demo.security.service;


import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.LoginUser;
import com.rbac_demo.entity.R;
import com.rbac_demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employeeByUserName = employeeService.findEmployeeByUserName(username);
        if (employeeByUserName == null) throw new CustomException("用户名或密码错误!");

        employeeService.fillEmpInfo(employeeByUserName);
        Collection<? extends GrantedAuthority> authorities = employeeService.getAuthorities(employeeByUserName);
        // 装入数据库中的用户信息和 他的权限
        return new LoginUser(employeeByUserName,authorities);
    }
}
