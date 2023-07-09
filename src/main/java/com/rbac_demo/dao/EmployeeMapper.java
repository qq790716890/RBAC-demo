package com.rbac_demo.dao;

import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@Mapper
public interface EmployeeMapper {


    Employee findEmployeeByUserName(String userName);

    int insertOne(Employee employee);

    List<Employee> selectByPage(int limit, int offset,String name);

    Employee selectOneById(Long id);

    int updateOne(Employee employee);

    int updateOneStatus(long id, int status);

    int updateOneByDel(long id);

    int selectCountByDepId(int id);

    int selectCountByJobId(int id);

    int selectAllCount(String name);

    Employee selectOneByIdForUpdate(Long id);
}
