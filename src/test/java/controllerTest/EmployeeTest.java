package controllerTest;

import com.rbac_demo.RbacApplication;
import com.rbac_demo.dao.EmployeeMapper;
import com.rbac_demo.entity.Employee;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author : lzy
 * @date : 2023/6/12
 * @effect :
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
public class EmployeeTest {

    @Autowired
    EmployeeMapper employeeMapper;



    @Test
    public void testInsert(){
        Employee employee = new Employee();
        employee.setName("admin");
        employee.setUserName("admin");
        employee.setPassword("admin123");
        employee.setStatus(1);
        employee.setDepartmentId(1);
        employee.setJobTitleId(1);
        employee.setCreateTime(new Date());

        employeeMapper.insertOne(employee);
    }

    @Test
    public void findByIdOrUserName(){
        Employee emp = employeeMapper.findEmployeeById(1);
        Assert.assertNotNull(emp);

        emp = employeeMapper.findEmployeeById(2);
        Assert.assertNull(emp);

        emp = employeeMapper.findEmployeeByUserName("admin");
        Assert.assertNotNull(emp);

        emp = employeeMapper.findEmployeeByUserName("addd");
        Assert.assertNull(emp);

    }

    @Test
    public void getAll(){
        List<Employee> allEmployee = employeeMapper.selectByPage(10,1,null);
        Assert.assertNotNull(allEmployee);

        allEmployee = employeeMapper.selectByPage(10,1,"admin");
        Assert.assertNotNull(allEmployee);

        allEmployee = employeeMapper.selectByPage(10,1,"不会存在的名字");
        Assert.assertEquals(0, allEmployee.size());

    }




}
