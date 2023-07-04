package com.rbac_demo.entity;


import java.util.HashMap;
import java.util.Map;

/**
 * @author : lzy
 * @date : 2023/6/9
 * @effect : 返回给前端的统一对象格式
 */

public class R<T>  {
    private Integer code; // 编码: 1成功 ；0和其他数字为失败

    private String msg;   // 错误信息

    private  T data;    // 数据

    private final Map<String,Object> map = new HashMap<>(); // 动态数据



    public static <T> R<T> success(T object){
        R<T> r = new R<>();
        r.code = 1;
        r.data = object;
        return r;
    }

    public static <T> R<T> error(String msg){
        R<T> r = new R<>();
        r.code = 0;
        r.data = (T) msg;
        return r;
    }

    public R<T> add(String key, Object val){
        this.map.put(key,val);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "R{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", map=" + map +
                '}';
    }
}
