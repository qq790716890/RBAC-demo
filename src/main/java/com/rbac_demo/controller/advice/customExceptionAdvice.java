package com.rbac_demo.controller.advice;


import com.rbac_demo.entity.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author : lzy
 * @date : 2023/6/27
 * @effect :
 */

@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class customExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(customExceptionAdvice.class);

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

//    @ExceptionHandler()
//    public R<String> handleException(Exception ex) {
//        log.error("[发生错误]： " + ex.getMessage());
//        return R.error(ex.getMessage());
//    }

}
