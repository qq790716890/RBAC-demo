package com.rbac_demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : lzy
 * @date : 2023/6/15
 * @effect : 访问接口需要的权限
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {
    String[] value();
    Logical logical() default Logical.AND;
    boolean rankCheck() default false;
}
