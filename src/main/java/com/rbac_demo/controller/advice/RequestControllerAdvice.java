package com.rbac_demo.controller.advice;


import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author : lzy
 * @date : 2023/6/11
 * @effect :
 */

@ControllerAdvice
public class RequestControllerAdvice {

    // 日志
    private static final Logger log = LoggerFactory.getLogger(RequestControllerAdvice.class);

    @ModelAttribute
    public void logRequest(HttpServletRequest request) {
        String service = request.getRequestURI(); // 获取请求的服务
        String msg = String.format("收到请求： " + "[%s]",service);
        log.debug(msg);
    }


}
