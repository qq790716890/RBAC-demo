package com.rbac_demo.security.filter;

import com.rbac_demo.common.CookieUtil;
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.dao.LoginTicketMapper;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.LoginTicket;
import com.rbac_demo.security.handler.LoginSuccessHandler;
import com.rbac_demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;



public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final LoginTicketMapper loginTicketMapper;

    private final EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    public TokenAuthenticationFilter(LoginTicketMapper loginTicketMapper, EmployeeService employeeService){
        this.loginTicketMapper = loginTicketMapper;
        this.employeeService = employeeService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        //  判断ticket 是否有效
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URL
        String requestURI = request.getRequestURI();

        log.info("Token Filter拦截到请求：{}",request.getRequestURI());


        String ticket = CookieUtil.getValue(request, "ticket");
        // 存在
        // 查询凭证
        LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
        // 检查凭证是否有效
        if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
            // 有效，根据凭证查询登录用户信息
            Employee loginEmp = employeeService.selectOneById(loginTicket.getUserId());
            employeeService.fillEmpInfo(loginEmp);
            // 在本次请求中持有用户

            SecurityContext newContext = SecurityContextHolder.createEmptyContext();

            // 这里传入三个参数代表已认证，传两个参数的话代表还未认证。 源码有标识。
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginEmp, null, employeeService.getAuthorities(loginEmp));
            newContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(newContext);
            EmployeeContext.setEmployee(loginEmp);
        }
//        }else{
//            // ticket 无效，执行后面的filter
//            filterChain.doFilter(request, response);
//        }
        filterChain.doFilter(request, response);
    }


}
