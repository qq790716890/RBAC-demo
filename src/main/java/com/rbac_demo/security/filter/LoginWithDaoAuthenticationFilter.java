package com.rbac_demo.security.filter;

import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.RSAUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class LoginWithDaoAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public LoginWithDaoAuthenticationFilter(){
        super.setPasswordParameter("password");
        super.setUsernameParameter("userName");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String username = request.getParameter(this.getUsernameParameter());
        username = username != null ? username : "";
        username = username.trim();
        String password = obtainPassword(httpRequest);
        password = password != null ? password : "";

        String decodePassword = null;
        try {
            decodePassword = RSAUtils.decryptBase64(password, EmployeeContext.getKeyPair().getPrivate());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, decodePassword);
            Authentication authenticate = this.getAuthenticationManager().authenticate(authRequest);
            successfulAuthentication(httpRequest, httpResponse, chain, authenticate);
        }catch (AuthenticationException e ){
            unsuccessfulAuthentication(httpRequest, httpResponse, e);
            return;
        }

        chain.doFilter(request,response);
    }






}

