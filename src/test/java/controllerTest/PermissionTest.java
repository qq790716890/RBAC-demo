package controllerTest;

import com.rbac_demo.RbacApplication;
import com.rbac_demo.aop.PermissionCheckAspect;
import com.rbac_demo.controller.EmployeeController;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : lzy
 * @date : 2023/6/15
 * @effect :
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
public class PermissionTest {

    @Autowired
    PermissionCheckAspect permissionCheckAspect;

    @Autowired
    EmployeeService employeeService;

    @Test
    public void testPerm(){

        Employee loginEmp = employeeService.selectOneById(1L);
        employeeService.fillEmpInfo(loginEmp);
        boolean ret = permissionCheckAspect.rankCheck(loginEmp, 6,EmployeeController.class);
        Assert.assertTrue(ret);

    }

}
