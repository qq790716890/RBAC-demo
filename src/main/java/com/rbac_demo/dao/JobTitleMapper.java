package com.rbac_demo.dao;


import com.rbac_demo.entity.JobTitle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@Mapper
public interface JobTitleMapper {


    JobTitle findJobTitleById(int id);

    List<JobTitle> selectByPage(int limit, int offset, String name);

    JobTitle selectOneById(int id);

    JobTitle selectOneByName(String name);

    int insertOne(JobTitle jobTitle);

    int updateOne(JobTitle jobTitle);

    int updateOneByDel(int id);

    List<JobTitle> selectLimitById(int id);

    int selectAllCount(String name);
}
