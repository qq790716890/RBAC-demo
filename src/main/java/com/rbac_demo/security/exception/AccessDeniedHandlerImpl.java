package com.rbac_demo.security.exception;

import com.alibaba.fastjson.JSON;
import com.rbac_demo.entity.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws  IOException {
        // 如果未登录则返回登录结果，通过输出流的方式向客户端页面响应数据
        log.info("没有访问权限");
        // 用户未登录，设置响应状态码为 401（未授权）
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        // 设置响应内容类型为 JSON
        response.setContentType("application/json");
        // 构建返回给前端的 JSON 数据，包含跳转登录页的信息
        // 将 JSON 数据写入响应输出流
        response.getWriter().write(JSON.toJSONString(R.error("没有访问权限")));
    }
}


