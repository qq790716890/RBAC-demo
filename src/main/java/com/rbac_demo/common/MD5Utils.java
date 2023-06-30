package com.rbac_demo.common;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * @author : lzy
 * @date : 2023/6/30
 * @effect :
 */
public class MD5Utils {

    // MD5加密
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
