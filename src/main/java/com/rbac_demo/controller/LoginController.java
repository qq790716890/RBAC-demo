package com.rbac_demo.controller;


import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.RSAUtils;
import com.rbac_demo.entity.R;

import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @author : lzy
 * @date : 2023/6/9
 * @effect : 用户登陆相关api
 */


@RestController
@CrossOrigin("*")
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee emp) throws Exception {
        Employee findEmp = employeeService.findEmployeeByUserName(emp.getUserName());

        PrivateKey aPrivate = EmployeeContext.getKeyPair().getPrivate();
        if (findEmp == null || !employeeService.checkRsaPassword(emp.getPassword(), findEmp.getPassword(), aPrivate)){
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

    @GetMapping("/getPublicKey")
    public Map<String,Object> getPublicKey() throws Exception {
        KeyPair keyPair = RSAUtils.getKeyPair();
        EmployeeContext.setKeyPair(keyPair);
        Map<String,Object> map = new HashMap<>();

        PublicKey publicKey = keyPair.getPublic();

        map.put("algorithm",publicKey.getAlgorithm());
        map.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        map.put("code",1);
        return map;
    }
}
