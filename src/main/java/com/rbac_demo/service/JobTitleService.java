package com.rbac_demo.service;

import com.rbac_demo.dao.DepartmentMapper;
import com.rbac_demo.dao.JobTitleMapper;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.JobTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@Service
public class JobTitleService {

    @Autowired
    private JobTitleMapper jobTitleMapper;


    public List<JobTitle> selectByPage(int limit, int offset, String name){
        return jobTitleMapper.selectByPage(limit,offset,name==null?null:"%"+name+"%");
    }


    public JobTitle selectOneByName(String name) {
        return jobTitleMapper.selectOneByName(name);
    }

    public JobTitle selectOneById(int id) {
        return jobTitleMapper.selectOneById(id);
    }

    public int insertOne(JobTitle jobTitle) {
        return jobTitleMapper.insertOne(jobTitle);
    }

    public int updateOne(JobTitle jobTitle) {
        return jobTitleMapper.updateOne(jobTitle);
    }

    public int deleteOneById(int id) {
        return jobTitleMapper.updateOneByDel(id);
    }

    public List<JobTitle> selectLimitById(int id) {
        return jobTitleMapper.selectLimitById(id);
    }

    public int selectAllCount(String name) {
        return jobTitleMapper.selectAllCount(name==null?null:"%"+name+"%");
    }
}
