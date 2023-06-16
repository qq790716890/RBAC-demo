package com.rbac_demo.controller.advice;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author : lzy
 * @date : 2023/6/11
 * @effect :
 */

@ControllerAdvice
public class requestControllerAdvice {

        @ModelAttribute
        public void logRequest(HttpServletRequest request) {
            String service = request.getRequestURI(); // 获取请求的服务
            System.out.println("收到请求： " + "[" + service + "]");
        }


}
