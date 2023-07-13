package com.rbac_demo.security;


import com.rbac_demo.dao.LoginTicketMapper;
import com.rbac_demo.security.exception.AccessDeniedHandlerImpl;
import com.rbac_demo.security.exception.AuthenticationEntryPointImpl;
import com.rbac_demo.security.filter.TokenAuthenticationFilter;
import com.rbac_demo.security.handler.LoginSuccessHandler;
import com.rbac_demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.annotation.Resource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

       // 不要交给spring管理，这样的话会导致该security filter 不受security的控制(他与security filterChain 同级别了)
    private TokenAuthenticationFilter tokenAuthenticationFilter;


    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private EmployeeService employeeService;



//    @Autowired
//    private AccessDeniedHandlerImpl accessDeniedHandler;

//    @Autowired
//    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // 静态资源放行
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/**/login*",
                "/**/logout*",
                "/**/getPublicKey",
                "/**/*.woff",
                "/**/*.ttf",
                "/**/*.woff2",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.jpeg",
                "/**/*.html"
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().authenticated();
//                // 对于登录接口 允许匿名访问
//                .authorizeRequests()
//                .antMatchers("/login").anonymous()
//                // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();


        http.addFilterBefore(new TokenAuthenticationFilter(loginTicketMapper,employeeService), UsernamePasswordAuthenticationFilter.class);

//        http.formLogin().loginProcessingUrl("/login");

        // 权限不足的两个
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl()).authenticationEntryPoint(new AuthenticationEntryPointImpl());

//                .permitAll();
//                配置认证成功处理器



//        http.logout().logoutUrl("/logout").permitAll();


    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler(){
//        return new LoginSuccessHandler();
//    }

//    @Bean
//    public LoginWithDaoAuthenticationFilter loginWithDaoAuthenticationFilter(){
//        return new LoginWithDaoAuthenticationFilter();
//    }



}
