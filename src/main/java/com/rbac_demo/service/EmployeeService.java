package com.rbac_demo.service;


import com.rbac_demo.common.PermissionUtils;
import com.rbac_demo.common.RSAUtils;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.dao.DepartmentMapper;
import com.rbac_demo.dao.EmployeeMapper;
import com.rbac_demo.dao.JobTitleMapper;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.JobTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.List;


/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private JobTitleMapper jobTitleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;


    public Employee findEmployeeByUserName(String userName) {
        return employeeMapper.findEmployeeByUserName(userName);
    }

    public boolean checkRsaPassword(String rsaBase64Password, String truePassword, PrivateKey privateKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String decrypted = RSAUtils.decryptBase64(rsaBase64Password, privateKey);
        return truePassword.equals(decrypted);
    }

    public void fillEmpInfo(Employee employee){
        JobTitle jobTitle = jobTitleMapper.findJobTitleById(employee.getJobTitleId());
        if (jobTitle == null) throw new CustomException("员工的职位ID不正确！");
        Department department = departmentMapper.findDepartmentById(employee.getDepartmentId());
        if (department == null) throw new CustomException("员工的部门ID不正确！");
        employee.setJobTitleName(jobTitle.getName());
        employee.setJobRank(jobTitle.getRank());
        employee.setDepartmentName(department.getName());
        employee.setDepRank(department.getRank());
        employee.setPermissions(PermissionUtils.string2Arr(jobTitle.getPermissions()));

    }

    public List<Employee> selectByPage(int limit,int offset,String name){
        return employeeMapper.selectByPage(limit,offset,name==null?null:"%"+name+"%");
    }

    public int selectAllCount(String name){
        return employeeMapper.selectAllCount(name==null?null:"%"+name+"%");
    }

    @Transactional
    public Employee selectOneById(Long id){
        return employeeMapper.selectOneById(id);
    }

    public int insertOne(Employee employee){
        return employeeMapper.insertOne(employee);
    }

    public int updateOne(Employee employee) {
        return employeeMapper.updateOne(employee);
    }

    public int updateOneStatus(long id, int status) {
        return employeeMapper.updateOneStatus(id,status);
    }

    public int deleteOneById(long id) {
        return employeeMapper.updateOneByDel(id);
    }

    public int selectCountByDepId(int id) {
        return employeeMapper.selectCountByDepId(id);
    }

    public int selectCountByJobId(int id) {
        return employeeMapper.selectCountByJobId(id);
    }

    public Employee selectOneByIdForUpdate(Long id) {
        return employeeMapper.selectOneByIdForUpdate(id);
    }
}
