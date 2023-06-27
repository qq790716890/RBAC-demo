package com.rbac_demo.controller.advice;

/**
 * @author : lzy
 * @date : 2023/6/27
 * @effect :
 */


public class CustomException extends RuntimeException{
    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

}
