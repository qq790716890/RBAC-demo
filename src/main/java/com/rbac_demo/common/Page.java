package com.rbac_demo.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/10
 * @effect : 分页对象
 */

@Data
public class Page<T> {
    //  发送的  post 请求：   currentPage: 1, pageSize: 10

    // 当前页码
    private Integer currentPage = 1;
    // 显示上限
    private Integer pageSize = 10;

    //搜索的名称
    private String name;

    // 数据总数（用于计算总页数）
    private Integer rows;

    // 每一项信息
    private List<T> records = new ArrayList<>();

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset(){
        // current * limit - limit
        return (currentPage-1) * pageSize;
    }
}
