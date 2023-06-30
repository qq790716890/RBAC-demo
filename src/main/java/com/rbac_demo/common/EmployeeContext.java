package com.rbac_demo.common;

import com.rbac_demo.entity.Employee;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.PrivateKey;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */



public class EmployeeContext {

//    static HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    public static void setEmployee(Employee employee) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("employee", employee);
    }

    public static Employee getEmployee() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (Employee) request.getSession().getAttribute("employee");
    }

    public static void clear() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().removeAttribute("employee");
        request.getSession().removeAttribute("keyPair");
    }

    public static void setKeyPair(KeyPair keyPair) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("keyPair", keyPair);
    }

    public static KeyPair getKeyPair() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (KeyPair) request.getSession().getAttribute("keyPair");
    }


}

