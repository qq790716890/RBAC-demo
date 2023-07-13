package com.rbac_demo.security.handler;

import com.rbac_demo.common.CommonUtils;
import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.dao.LoginTicketMapper;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.LoginTicket;
import com.rbac_demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private EmployeeService employeeService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final Logger log = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        handler(response, authentication);
    }

    private void handler(HttpServletResponse response, Authentication authentication) {
        log.info("认证成功了");

        // 登陆成功
        // 设置 ticket cookie
        // 登录成功，生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        Employee loginEmp = (Employee) authentication.getPrincipal();
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
        newContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(newContext);
    }

}
