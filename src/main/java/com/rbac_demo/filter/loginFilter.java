package com.rbac_demo.filter;

import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import com.alibaba.fastjson.JSON;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : lzy
 * @date : 2023/6/15
 * @effect : 查看请求 + 对登录拦截
 */

@WebFilter(filterName = "logInFilter",urlPatterns = "/*")
@Slf4j
@Order(1)
public class loginFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URL
        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}",request.getRequestURI());

        // 定义不需要处理的请求路径
        String[] urls= new String[]{
                "/**/login*",
                "/**/*.woff",
                "/**/*.ttf",
                "/**/*.woff2",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.jpeg"
        };

        //2.判断本次请求是否需要处理
        boolean pass = check(urls, requestURI);

        //3.如果不需要处理，则直接放行
        if (pass){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return ;
        }

        //4-1.判断登录状态，如果已登录，则直接放行
        if (EmployeeContext.getEmployee() !=null){
            log.info("用户已登录，用户id为:{}",EmployeeContext.getEmployee().getId());
            filterChain.doFilter(request,response);
            return ;
        }

        //5.如果未登录则返回登录结果，通过输出流的方式向客户端页面响应数据
        log.info("用户未登录");
        // 用户未登录，设置响应状态码为 401（未授权）
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 设置响应内容类型为 JSON
        response.setContentType("application/json");
        // 构建返回给前端的 JSON 数据，包含跳转登录页的信息
        // 将 JSON 数据写入响应输出流
//        response.getWriter().write(loginPageResponse);
        response.getWriter().write(JSON.toJSONString(R.error("NOT LOGIN")));
    }

    public boolean check(String[] urls,String requestURI){
        for (String url:urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }

}