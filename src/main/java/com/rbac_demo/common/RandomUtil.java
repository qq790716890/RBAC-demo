package com.rbac_demo.common;
import java.util.Random;

/**
 * @author : lzy
 * @date : 2023/7/8
 * @effect :
 */
public class RandomUtil {
    static Random random = new Random();
    public static int getRandInt(){
        return random.nextInt(Integer.MAX_VALUE);
    }

    public static String getRandString(){
        return CommonUtils.generateUUID().substring(0,9);
    }
}
