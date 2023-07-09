package com.rbac_demo.controller;


import com.rbac_demo.common.CommonUtils;
import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.RSAUtils;
import com.rbac_demo.dao.LoginTicketMapper;
import com.rbac_demo.entity.LoginTicket;
import com.rbac_demo.entity.R;

import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
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

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee emp, HttpServletResponse response) throws Exception {
        Employee findEmp = employeeService.findEmployeeByUserName(emp.getUserName());

        PrivateKey aPrivate = EmployeeContext.getKeyPair().getPrivate();
        if (findEmp == null || !employeeService.checkRsaPassword(emp.getPassword(), findEmp.getPassword(), aPrivate)){
            return R.error("用户或密码错误！");
        }
        if (findEmp.getStatus()==0){
            return R.error("用户已被禁用，请联系管理员！！！");
        }

        // 登陆成功
        // 设置 ticket cookie
        // 登录成功，生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(findEmp.getId());
        loginTicket.setTicket(CommonUtils.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + ConstantUtils.DEFAULT_EXPIRE_SECONDS * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        // 设置 cookie
        Cookie cookie = new Cookie("ticket",loginTicket.getTicket());
        cookie.setPath(contextPath);
        cookie.setMaxAge(ConstantUtils.DEFAULT_EXPIRE_SECONDS);
        response.addCookie(cookie);

        employeeService.fillEmpInfo(findEmp);
        EmployeeContext.setEmployee(findEmp);
        return R.success(findEmp);
    }

    @PostMapping("/logout")
    public R<String> logout(@CookieValue("ticket") String ticket){
        EmployeeContext.clear();
        // ticket 状态设置为1，表示失效
        loginTicketMapper.updateStatus(ticket,1);
        return R.success("退出登陆成功!");
    }

    @GetMapping("/getPublicKey")
    public Map<String,Object> getPublicKey() throws NoSuchAlgorithmException {
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
