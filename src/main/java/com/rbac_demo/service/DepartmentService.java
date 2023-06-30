package com.rbac_demo.service;

import com.rbac_demo.dao.DepartmentMapper;
import com.rbac_demo.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;


    public List<Department> selectByPage(int limit,int offset,String name){
        return departmentMapper.selectByPage(limit,offset,name==null?null:"%"+name+"%");
    }

    public int insertOne(Department department){
        return departmentMapper.insertOne(department);
    }


    public int updateOne(Department department) {
        return departmentMapper.updateOne(department);
    }

    public Department selectOneById(int id) {
        return departmentMapper.selectOneById(id);
    }

    public Department selectOneByName(String name) {
        return departmentMapper.selectOneByName(name);
    }

    public int deleteOneById(int id) {
        return departmentMapper.updateOneByDel(id);
    }

    public List<Department> selectLimitById(int id) {
        return departmentMapper.selectLimitById(id);
    }

    public int selectAllCount(String name) {
        return departmentMapper.selectAllCount(name==null?null:"%"+name+"%");
    }
}
