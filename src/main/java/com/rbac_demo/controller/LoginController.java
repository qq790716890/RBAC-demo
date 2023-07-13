package com.rbac_demo.controller;


import com.mysql.cj.log.Log;
import com.rbac_demo.VO.LoginVO;
import com.rbac_demo.common.*;
import com.rbac_demo.dao.LoginTicketMapper;
import com.rbac_demo.entity.LoginTicket;
import com.rbac_demo.entity.LoginUser;
import com.rbac_demo.entity.R;

import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.*;
import java.util.*;


/**
 * @author : lzy
 * @date : 2023/6/9
 * @effect : 用户登陆相关api
 */


@RestController
@CrossOrigin("*")
@Validated
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${server.servlet.context-path}")
    private String contextPath;



    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public R<Employee> login(@RequestBody @Valid LoginVO loginVO, HttpServletResponse response) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        String decodePassword = RSAUtils.decryptBase64(loginVO.getPassword(), EmployeeContext.getKeyPair().getPrivate());
//        String salt = employeeService.selectSaltByUserName(loginVO.getUserName());  // 不需要自己加盐, security 自带的加密算法会加盐了已经
//        String saltHashPassword = passwordEncoder.encode(decodePassword );

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVO.getUserName(),decodePassword);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        LoginUser loginDto = (LoginUser) authenticate.getPrincipal();

        log.info("认证成功了");
        // 登陆成功
        // 设置 ticket cookie
        // 登录成功，生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        Employee loginEmp = loginDto.getEmployee();
        loginTicket.setUserId(loginEmp.getId());
        loginTicket.setTicket(CommonUtils.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + ConstantUtils.DEFAULT_EXPIRE_SECONDS * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        // 设置 cookie
        Cookie cookie = new Cookie("ticket",loginTicket.getTicket());
        cookie.setPath(contextPath);
        cookie.setMaxAge(ConstantUtils.DEFAULT_EXPIRE_SECONDS);
        response.addCookie(cookie);

        employeeService.fillEmpInfo(loginEmp);
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authenticate);
        SecurityContextHolder.setContext(newContext);

        return R.success(loginDto.getEmployee());
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
