package com.rbac_demo.dao;

import com.rbac_demo.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@Mapper
public interface DepartmentMapper {

    Department findDepartmentById(int id);

    List<Department> selectByPage(int limit, int offset,String name);

    int  insertOne(Department department);

    int updateOne(Department department);

    Department selectOneById(int id);

    Department selectOneByName(String name);

    int deleteOneById(int id);

    List<Department> selectLimitById(int id);

    int selectAllCount(String name);
}
