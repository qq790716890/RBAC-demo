package com.rbac_demo.controller;

import com.rbac_demo.annotation.Logical;
import com.rbac_demo.annotation.RequiresPermissions;
import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.common.Page;
import com.rbac_demo.entity.R;
import com.rbac_demo.entity.Department;
import com.rbac_demo.service.DepartmentService;
import com.rbac_demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/11
 * @effect :
 */

@RestController
@RequestMapping("/department")
public class DepartmentController implements ConstantUtils {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    // 日志


    @RequiresPermissions(DEP_READ)
    @PostMapping("/list")
    public R<Page<Department>> list(@RequestBody Page<Department> page){
        if (page == null || page.getPageSize() == null || page.getCurrentPage()==null ) return R.error("请求参数不合法！");

        List<Department> departments = departmentService.selectByPage(page.getPageSize(), page.getOffset(),page.getName());
        int rows = departmentService.selectAllCount(page.getName());
        page.setRows(rows);
        page.setRecords(departments);
        return R.success(page);
    }

    @RequiresPermissions(value = {DEP_INSERT,DEP_UPDATE}, logical = Logical.OR,rankCheck = true)
    @PostMapping("/add")
    public R<String> add(@RequestBody Department department){
        if (department == null) return R.error("传参为空！");


        // 置空，防止用户给定
        department.setCreateTime(null);
        department.setCreateUserId(null);
        department.setUpdateUserId(null);
        if (department.getId() == null) department.setUpdateTime(null); // 更新操作的话，不需要设置为空，会查数据库校验

        // 根据是否存在id，来判断是插入还是更新
        if (department.getId() == null){
            // 部门名字不能冲突，必须唯一，唯一性判断在数据库增面设置了，且通过全局异常捕获了
            int ret = departmentService.insertOne(department);
            if (ret!=0){
                return R.success("新增部门成功!");
            }
            return R.error("新增部门失败!");
        }else{
            int ret = departmentService.updateOne(department);
            if (ret!=0){
                return R.success("更新部门成功!");
            }
            return R.error("更新部门失败!");
        }
    }

    @RequiresPermissions(value = {DEP_READ},rankCheck = true)
    @GetMapping("/query/{id}")
    public R<Department> queryOneById(@PathVariable("id")  int id){
        Department department = departmentService.selectOneById(id);
//        if (department == null) return R.error("部门不存在！");  会在AOP权限校验那里，就判断到了不存在
        return R.success(department);
    }

    @RequiresPermissions(value = {DEP_DELETE},rankCheck = true)
    @DeleteMapping("/delete/{id}")
    public R<String> deleteOneById(@PathVariable("id")  int id){
        // 删除前，检查是否还存在该部门的的员工
        int cnt = employeeService.selectCountByDepId(id);
        if (cnt >0) return R.error("不能删除该部门，还有员工在该部门中！");
        int ret = departmentService.deleteOneById(id);
        if (ret == 0){
            return R.error("删除失败!");
        }
        return R.success("删除成功!");
    }


    @RequiresPermissions(value = {DEP_READ},rankCheck = true)
    @GetMapping("/queryLimitDepartment/{id}")
    public R<List<Department>> queryLimitDepartment(@PathVariable("id") int id){
        List<Department> ls =  departmentService.selectLimitById(id);
        return R.success(ls);
    }

}
