package com.rbac_demo.controller.advice;

import com.rbac_demo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author : lzy
 * @date : 2023/6/27
 * @effect :
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class customExceptionAdvice {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<R<String>> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2]+"已存在";
            return new ResponseEntity<>(R.error(ex.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(R.error("未知错误"),HttpStatus.OK);
    }

    @ExceptionHandler()
    public ResponseEntity<R<String>> handleException(Exception ex) {
        log.error("[发生错误]： " + ex.getMessage());
        return new ResponseEntity<>(R.error(ex.getMessage()), HttpStatus.OK);
    }

}
