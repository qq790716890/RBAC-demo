package com.rbac_demo.security.exception;

import com.alibaba.fastjson.JSON;
import com.rbac_demo.entity.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationEntryPointImpl.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 如果未登录则返回登录结果，通过输出流的方式向客户端页面响应数据
        log.info("用户未登录");
        // 用户未登录，设置响应状态码为 401（未授权）
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        // 设置响应内容类型为 JSON
        response.setContentType("application/json");
        // 构建返回给前端的 JSON 数据，包含跳转登录页的信息
        // 将 JSON 数据写入响应输出流
        response.getWriter().write(JSON.toJSONString(R.error("NOT LOGIN")));
    }
}


