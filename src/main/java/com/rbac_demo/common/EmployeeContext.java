package com.rbac_demo.common;


import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Employee;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;


/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */


public class EmployeeContext {
    public static final String KEY_PAIR = "keyPair";
    private static final String EMP = "employee";
    private static final String ERR = "request属性为空！";

    private EmployeeContext() {
    }

    public static void setEmployee(Employee employee) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null || requestAttributes.getRequest().getSession() == null) throw new CustomException(ERR);
        requestAttributes.getRequest().getSession().setAttribute(EMP, employee);
    }

    public static Employee getEmployee() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null || requestAttributes.getRequest().getSession() == null) throw new CustomException(ERR);

        return (Employee) requestAttributes.getRequest().getSession().getAttribute(EMP);
    }

    public static void clear() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null || requestAttributes.getRequest().getSession() == null) throw new CustomException(ERR);

        HttpServletRequest request = requestAttributes.getRequest();
        request.getSession().removeAttribute(EMP);
        request.getSession().removeAttribute(KEY_PAIR);
    }

    public static void setKeyPair(KeyPair keyPair) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null || requestAttributes.getRequest().getSession() == null) throw new CustomException(ERR);

        requestAttributes.getRequest().getSession().setAttribute(KEY_PAIR, keyPair);

    }

    public static KeyPair getKeyPair() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null || requestAttributes.getRequest().getSession() == null) throw new CustomException(ERR);
        HttpServletRequest request = requestAttributes.getRequest();
        return (KeyPair) request.getSession().getAttribute(KEY_PAIR);
    }


}

