package com.rbac_demo.controller;

import com.rbac_demo.annotation.Logical;
import com.rbac_demo.annotation.RequiresPermissions;
import com.rbac_demo.common.ConstantUtils;
import com.rbac_demo.common.Page;
import com.rbac_demo.common.R;
import com.rbac_demo.dao.JobTitleMapper;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.JobTitle;
import com.rbac_demo.service.EmployeeService;
import com.rbac_demo.service.JobTitleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@RestController
@RequestMapping("/jobTitle")
@Slf4j
public class JobTitleController implements ConstantUtils {

    @Autowired
    private JobTitleService jobTitleService;

    @Autowired
    private EmployeeService employeeService;


    @RequiresPermissions(value = {JOBTITLE_READ})
    @PostMapping("/list")
    public R<Page> list(@RequestBody Page page){
        List<JobTitle> jobTitles = jobTitleService.selectByPage(page.getPageSize(), page.getOffset(),page.getName());
        int rows = jobTitleService.selectAllCount(page.getName());
        page.setRows(rows);
        page.setRecords(jobTitles);
        return R.success(page);
    }


    @RequiresPermissions(value = {JOBTITLE_UPDATE,JOBTITLE_INSERT},rankCheck = true,logical = Logical.OR)
    @PostMapping("/add")
    public R<String> add(@RequestBody JobTitle jobTitle){
        // 根据是否存在id，来判断是插入还是更新
        if (jobTitle.getId() == null){
            // 部门名字不能冲突，必须唯一
            JobTitle jobTitle1 = jobTitleService.selectOneByName(jobTitle.getName());
            if (jobTitle1!=null){
                return R.error("职位名称冲突，请换个职位名称！");
            }
            int ret = jobTitleService.insertOne(jobTitle);
            if (ret!=0){
                return R.success("新增职位成功");
            }
            return R.error("新增职位失败！");
        }else{
            int ret = jobTitleService.updateOne(jobTitle);
            if (ret!=0){
                return R.success("更新职位成功");
            }
            return R.error("更新职位失败！");
        }
    }


    @RequiresPermissions(value = {JOBTITLE_READ},rankCheck = true)
    @GetMapping("/query/{id}")
    public R<JobTitle> queryOneById(@PathVariable("id")  int id){
        JobTitle jobTitle = jobTitleService.selectOneById(id);
        if (jobTitle == null) return R.error("部门不存在！");
        return R.success(jobTitle);
    }

    @RequiresPermissions(value = {JOBTITLE_DELETE},rankCheck = true)
    @DeleteMapping("/delete/{id}")
    public R<String> deleteOneById(@PathVariable("id")  int id){
        // 删除前，检查是否还存在该jobTitle的员工
        int cnt = employeeService.selectCountByJobId(id);
        if (cnt >0) return R.error("不能删除该职位，还有员工在该职位上！");
        int ret = jobTitleService.deleteOneById(id);
        if (ret == 0){
            return R.error("删除失败！");
        }
        return R.success("删除成功");
    }

    @RequiresPermissions(value = {JOBTITLE_READ},rankCheck = true)
    @GetMapping("/queryLimitJobTitles/{id}")
    public R<List<JobTitle>> queryLimitJobTitles(@PathVariable("id") int id){
        List<JobTitle> ls =  jobTitleService.selectLimitById(id);
        return R.success(ls);
    }


}
