package com.rbac_demo.controller.advice;


import com.rbac_demo.entity.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author : lzy
 * @date : 2023/6/27
 * @effect :
 */

@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class CustomExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionAdvice.class);

    @ExceptionHandler(SQLException.class)
    public R<String> exceptionHandler(SQLException ex){
        log.error(ex.getMessage());
        if (ex instanceof SQLIntegrityConstraintViolationException) {
            if (ex.getMessage().contains("Duplicate entry")) {
                String[] split = ex.getMessage().split(" ");
                String dup = split[2].split("-")[0].substring(1);
                String msg = "[" + dup + "]"+ "已存在";
                return R.error(msg);
            }
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> handleException(CustomException ex) {
        String msg = String.format("[发生错误]： %s",ex.getMessage());
        log.error(msg);
        return R.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> handleArgumentsException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return R.error("请检查请求参数是否正确!");
    }



}
