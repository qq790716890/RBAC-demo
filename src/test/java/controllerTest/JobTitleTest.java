package controllerTest;

import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.PermissionUtils;
import com.rbac_demo.dao.EmployeeMapper;
import com.rbac_demo.dao.JobTitleMapper;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.JobTitle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
public class JobTitleTest {

    @Autowired
    JobTitleMapper jobTitleMapper;



    @Test
    public void testInsert(){
        JobTitle jobTitle = new JobTitle();
        jobTitle.setName("系统管理");
        jobTitle.setRank(0);
        jobTitle.setDescription("系统管理");

        String permissions = PermissionUtils.getAllDepartmentPermissions() + "," + PermissionUtils.getAllEmployeePermissions()+","+ PermissionUtils.getAllJobTitlePermissions();
        jobTitle.setPermissions(permissions);
        jobTitle.setCreateTime(new Date());
        jobTitleMapper.insertOne(jobTitle);
    }

    @Test
    public void ss(){

        String[] ls = new String[]{"EMPLOYEE_READ","EMPLOYEE_WRITE",""};
        String result = String.join(",", ls);
        System.out.println(result);
        System.out.println();
        String[] split = result.split(",");
        List<String> resultList = Arrays.asList(split);
        System.out.println();
    }


}
