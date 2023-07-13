package com.rbac_demo.controller;

import com.rbac_demo.VO.StatusVO;
import com.rbac_demo.annotation.Logical;
import com.rbac_demo.annotation.RequiresPermissions;

import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.common.Page;
import com.rbac_demo.entity.R;
import com.rbac_demo.entity.Employee;

import com.rbac_demo.service.EmployeeService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/10
 * @effect : 员工管理
 */

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController implements ConstantUtils {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //    @RequiresPermissions(value = {EMP_READ})
    @PreAuthorize("hasAnyAuthority('EMPLOYEE_READ')")
    @PostMapping("/list")
    public R<Page<Employee>> list(@RequestBody Page<Employee> page) {
        if (page.getPageSize() == null || page.getCurrentPage() == null) return R.error("请求参数不合法！");
        List<Employee> employees = employeeService.selectByPage(page.getPageSize(), page.getOffset(), page.getName());


        employees.forEach(employee ->
                // 对每个元素增加信息
                // 假设增加信息的方法为addAdditionalInfo()
                employeeService.fillEmpInfo(employee)
        );

        int rows = employeeService.selectAllCount(page.getName());
        page.setRows(rows);
        page.setRecords(employees);
        return R.success(page);
    }

    //    @RequiresPermissions(value = {EMP_READ},rankCheck = true)
    @RequiresPermissions(value = {}, rankCheck = true)
    @GetMapping("/query/{id}")
    @PreAuthorize("hasAnyAuthority('" + EMP_READ + "')")
    public R<Employee> queryOne(@PathVariable("id") Long id) {
        Employee employee = employeeService.selectOneById(id);
//        if (employee == null) return R.error("用户不存在！");    会在AOP权限校验那里，就判断到了不存在
        employeeService.fillEmpInfo(employee);
        return R.success(employee);
    }

    //    @RequiresPermissions(value = {EMP_INSERT,EMP_UPDATE},rankCheck = true,logical = Logical.OR)
    @PreAuthorize("hasAnyAuthority('" + EMP_UPDATE + "', '" + EMP_INSERT + "')")
    @RequiresPermissions(value = {}, rankCheck = true)
    @PostMapping("/add")
    public R<String> add(@RequestBody @Valid Employee employee) {

        // 置空，防止用户给定
        employee.setCreateTime(null);
        employee.setCreateUserId(null);
        employee.setUpdateUserId(null);
        // 密码加密
        if (!StringUtils.isBlank(employee.getPassword())) {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }

        if (employee.getId() == null) employee.setUpdateTime(null); // 更新操作的话，不需要设置为空，会查数据库校验
        // 根据是否存在id，来判断是插入还是更新
        if (employee.getId() == null) {
            int ret = employeeService.insertOne(employee);
            if (ret != 0) {
                return R.success("新增员工成功");
            }
            return R.error("新增员工失败！");
        } else {
            int ret = employeeService.updateOne(employee);
            if (ret != 0) {
                return R.success("更新员工成功");
            }
            return R.error("更新员工失败！");
        }
    }

    //    @RequiresPermissions(value = {EMP_READ},rankCheck = true)
    @RequiresPermissions(value = {}, rankCheck = true)
    @PreAuthorize("hasAnyAuthority('" + EMP_UPDATE + "', '" + EMP_INSERT + "')")
    @PutMapping("/updateStatus/{id}")
    public R<String> updateStatus(@PathVariable("id") long id, @RequestBody @Valid StatusVO statusVO) {
        int ret = employeeService.updateOneStatus(id, statusVO.getStatus());
        if (ret == 1) {
            return R.success("更新状态成功！");
        }
        return R.error("更新状态失败！");
    }

    //    @RequiresPermissions(value = {EMP_DELETE},rankCheck = true)
    @RequiresPermissions(value = {}, rankCheck = true)
    @PreAuthorize("hasAnyAuthority('" + EMP_DELETE + "')")
    @DeleteMapping("/delete/{id}")
    public R<String> deleteOneById(@PathVariable("id") long id) {
        int ret = employeeService.deleteOneById(id);
        if (ret == 0) {
            return R.error("删除失败!");
        }
        return R.success("删除成功!");
    }


}
