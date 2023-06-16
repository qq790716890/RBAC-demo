package com.rbac_demo.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author : lzy
 * @date : 2023/6/10
 * @effect :
 */

//@Configuration
//@WebFilter(filterName = "requestFilter",urlPatterns = "/*")
//@Order(1)
public class requestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 读取请求体内容
        String requestBody = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            requestBody = httpRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        }

        // 处理请求体内容，例如打印日志或进行其他操作
        if (requestBody != null) {
            System.out.println("Request Body: " + requestBody);
        }
        chain.doFilter(request,response);
    }

}