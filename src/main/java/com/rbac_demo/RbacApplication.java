package com.rbac_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author : lzy
 * @date : 2023/6/9
 * @effect : 当前类的作用是
 */

@SpringBootApplication
@ServletComponentScan   // 该注解不加，@W
public class RbacApplication {
    public static void main(String[] args) {
        SpringApplication.run(RbacApplication.class);
    }
}
