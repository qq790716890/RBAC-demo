package com.rbac_demo.controller;

import com.rbac_demo.annotation.Logical;
import com.rbac_demo.annotation.RequiresPermissions;
import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.common.Page;
import com.rbac_demo.common.R;
import com.rbac_demo.entity.Employee;

import com.rbac_demo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : lzy
 * @date : 2023/6/10
 * @effect : 员工管理
 */

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController implements ConstantUtils {

    @Autowired
    private EmployeeService employeeService;


    @RequiresPermissions(value = {EMP_READ})
    @PostMapping("/list")
    public R<Page> list(@RequestBody Page page){
        if (page == null || page.getPageSize() == null || page.getCurrentPage()==null ) throw new IllegalArgumentException();
        List<Employee> employees = employeeService.selectByPage(page.getPageSize(), page.getOffset(),page.getName());

        List<Employee> updatedEmployees = employees.stream()
                .peek(employee -> {
                    // 对每个元素增加信息
                    // 假设增加信息的方法为addAdditionalInfo()
                    employeeService.fillEmpInfo(employee);
                })
                .collect(Collectors.toList());
        int rows = employeeService.selectAllCount(page.getName());
        page.setRows(rows);
        page.setRecords(updatedEmployees);
        return R.success(page);
    }

    @RequiresPermissions(value = {EMP_READ},rankCheck = true)
    @GetMapping("/query/{id}")
    public R<Employee> queryOne(@PathVariable("id")  Long id){
        Employee employee = employeeService.selectOneById(id);
        if (employee == null) return R.error("用户不存在！");
        employeeService.fillEmpInfo(employee);
        return R.success(employee);
    }

    @RequiresPermissions(value = {EMP_INSERT,EMP_UPDATE},rankCheck = true,logical = Logical.OR)
    @PostMapping("/add")
    public R<String> add(@RequestBody Employee employee){
        if (employee == null) throw new IllegalArgumentException("参数为空！");

        // 置空，防止用户给定
        employee.setCreateTime(null);
        employee.setCreateUserId(null);
        employee.setUpdateUserId(null);
        if (employee.getId() == null) employee.setUpdateTime(null); // 更新操作的话，不需要设置为空，会查数据库校验
        // 根据是否存在id，来判断是插入还是更新
        if (employee.getId() == null){
            int ret = employeeService.insertOne(employee);
            if (ret!=0){
                return R.success("新增员工成功");
            }
            return R.error("新增员工失败！");
        }else{
            int ret = employeeService.updateOne(employee);
            if (ret!=0){
                return R.success("更新员工成功");
            }
            return R.error("更新员工失败！");
        }
    }

    @RequiresPermissions(value = {EMP_READ},rankCheck = true)
    @PutMapping("/updateStatus/{id}")
    public R<String> updateStatus(@PathVariable("id") int id, @RequestBody Map<String, Integer> map){
        if (map == null) throw new IllegalArgumentException("输入为空！");
        int status = map.get("status");
        int ret = employeeService.updateOneStatus(id,status);
        if (ret == 1){
            return R.success("更新状态成功！");
        }
        return R.error("更新状态失败！");
    }

    @RequiresPermissions(value = {EMP_DELETE},rankCheck = true)
    @DeleteMapping("/delete/{id}")
    public R<String> deleteOneById(@PathVariable("id")  int id){
        int ret = employeeService.deleteOneById(id);
        if (ret == 0){
            return R.error("删除失败！");
        }
        return R.success("删除成功");
    }




}
