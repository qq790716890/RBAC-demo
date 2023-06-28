package com.rbac_demo.common;



import java.util.ArrayList;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/10
 * @effect : 分页对象
 */

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

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Page{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", name='" + name + '\'' +
                ", rows=" + rows +
                ", records=" + records +
                '}';
    }
}
