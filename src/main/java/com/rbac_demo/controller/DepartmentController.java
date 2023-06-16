package com.rbac_demo.controller;

import com.rbac_demo.annotation.Logical;
import com.rbac_demo.annotation.RequiresPermissions;
import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.common.Page;
import com.rbac_demo.common.R;
import com.rbac_demo.entity.Department;
import com.rbac_demo.service.DepartmentService;
import com.rbac_demo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DepartmentController implements ConstantUtils {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @RequiresPermissions(DEP_READ)
    @PostMapping("/list")
    public R<Page> list(@RequestBody Page page){
        List<Department> departments = departmentService.selectByPage(page.getPageSize(), page.getOffset(),page.getName());
        int rows = departmentService.selectAllCount(page.getName());
        page.setRows(rows);
        page.setRecords(departments);
        return R.success(page);
    }

    @RequiresPermissions(value = {DEP_INSERT,DEP_UPDATE}, logical = Logical.OR,rankCheck = true)
    @PostMapping("/add")
    public R<String> add(@RequestBody Department department){
        // 根据是否存在id，来判断是插入还是更新
        if (department.getId() == null){
            // 部门名字不能冲突，必须唯一
            Department department1 = departmentService.selectOneByName(department.getName());
            if (department1!=null){
                return R.error("部门名称冲突，请换个部门名称！");
            }
            int ret = departmentService.insertOne(department);
            if (ret!=0){
                return R.success("新增部门成功");
            }
            return R.error("新增部门失败！");
        }else{
            int ret = departmentService.updateOne(department);
            if (ret!=0){
                return R.success("更新部门成功");
            }
            return R.error("更新部门失败！");
        }
    }

    @RequiresPermissions(value = {DEP_READ},rankCheck = true)
    @GetMapping("/query/{id}")
    public R<Department> queryOneById(@PathVariable("id")  int id){
        Department department = departmentService.selectOneById(id);
        if (department == null) return R.error("部门不存在！");
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
            return R.error("删除失败！");
        }
        return R.success("删除成功");
    }


    @RequiresPermissions(value = {DEP_READ},rankCheck = true)
    @GetMapping("/queryLimitDepartment/{id}")
    public R<List<Department>> queryLimitDepartment(@PathVariable("id") int id){
        List<Department> ls =  departmentService.selectLimitById(id);
        return R.success(ls);
    }

}
