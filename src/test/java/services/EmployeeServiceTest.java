package services;

/**
 * @author : lzy
 * @date : 2023/7/7
 * @effect :
 */

import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.CommonUtils;
import com.rbac_demo.common.EmployeeContext;

import com.rbac_demo.common.RandomUtil;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;
import org.junit.After;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Random;

@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
@ActiveProfiles("test")
@Transactional
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    private static Employee loginEmp;

    private Employee insertEmp;

    private static Random random;

    private static MockedStatic<EmployeeContext> employeeContextMockedStatic;


    @BeforeAll
    public static void setup2(){
        random = new Random();
        loginEmp = new Employee();
        loginEmp.setId(1L);
    }
    @AfterEach
    public void after(){
        employeeContextMockedStatic.close();
    }

    public int getRandId(){
        return random.nextInt();
    }


    @BeforeEach
    public  void setup() throws Exception {
        // AOP 中需要 EmployeeContext 取当前登陆用户
        employeeContextMockedStatic = Mockito.mockStatic(EmployeeContext.class);
        employeeContextMockedStatic.when(EmployeeContext::getEmployee).thenReturn(loginEmp);

        insertEmp = new Employee();
        insertEmp.setUserName(CommonUtils.generateUUID().substring(0,15));
        insertEmp.setName("name");
        insertEmp.setPassword(CommonUtils.generateUUID().substring(0,15));
        insertEmp.setStatus(0);
        insertEmp.setDepartmentId(getRandId());
        insertEmp.setJobTitleId(getRandId());
    }

    public void assertionExceptTime(Employee expect, Employee obt){
        // 判断插入的数据是否一致
        Assertions.assertAll("Department properties",
                () -> Assertions.assertEquals(expect.getId(), obt.getId()),
                () -> Assertions.assertEquals(expect.getName(), obt.getName()),
                () -> Assertions.assertEquals(expect.getCreateUserId(), obt.getCreateUserId()),
                () -> Assertions.assertEquals(expect.getUserName(), obt.getUserName()),
                () -> Assertions.assertEquals(expect.getPassword(), obt.getPassword()),
                () -> Assertions.assertEquals(expect.getJobRank(), obt.getJobRank()),
                () -> Assertions.assertEquals(expect.getDepRank(), obt.getDepRank()),
                () -> Assertions.assertEquals(expect.getJobTitleId(), obt.getJobTitleId()),
                () -> Assertions.assertEquals(expect.getDepartmentId(), obt.getDepartmentId()),
                () -> Assertions.assertEquals(expect.getDepartmentName(), obt.getDepartmentName()),
                () -> Assertions.assertEquals(expect.getPermissions(), obt.getPermissions()),
                () -> Assertions.assertEquals(expect.getStatus(), obt.getStatus()),
                () -> Assertions.assertEquals(expect.getUpdateUserId(), obt.getUpdateUserId())
        );
    }


    @Test
    void test_selectByPage(){
        // 查找
        List<Employee> results = employeeService.selectByPage(10, 0, null);
        Assertions.assertEquals(10,results.size());

        List<Employee> results2 = employeeService.selectByPage(10, 0, "专门管理");
        Assertions.assertEquals(3,results2.size());
    }



    @Test
    void test_findEmployeeByUserName(){
        employeeService.insertOne(insertEmp);
        Employee ret = employeeService.findEmployeeByUserName(insertEmp.getUserName());
        assertionExceptTime(insertEmp,ret);

        // 查不存在
        Employee ret2 = employeeService.findEmployeeByUserName(CommonUtils.generateUUID().substring(0,20));
        Assertions.assertNull(ret2);

    }

    @Test
    void test_selectOneById(){
        employeeService.insertOne(insertEmp);
        Employee ret = employeeService.selectOneById(insertEmp.getId());
        assertionExceptTime(insertEmp,ret);

        // 查不存在
        Employee ret2 = employeeService.selectOneById((long) Integer.MAX_VALUE);
        Assertions.assertNull(ret2);

    }

    @Test
    void test_insertOne(){
        employeeService.insertOne(insertEmp);
        Employee ret = employeeService.selectOneById(insertEmp.getId());
        assertionExceptTime(insertEmp,ret);
    }

    @Test
    void test_updateOne(){
        employeeService.insertOne(insertEmp);
        insertEmp.setJobTitleId(23);
        insertEmp.setName("mnihao");
        employeeService.updateOne(insertEmp);
        Employee ret = employeeService.selectOneById(insertEmp.getId());
        assertionExceptTime(insertEmp,ret);
    }

    @Test
    void test_updateOneStatus(){
        employeeService.insertOne(insertEmp);
        employeeService.updateOneStatus(insertEmp.getId(),1);

        Employee ret = employeeService.selectOneById(insertEmp.getId());
        Assertions.assertEquals(1,ret.getStatus());
    }

    @Test
    void test_deleteOneById(){
        employeeService.insertOne(insertEmp);
        Assertions.assertNotNull(insertEmp.getId());
        int i = employeeService.deleteOneById(insertEmp.getId());
        Assertions.assertNotEquals(0,i);
    }

    @Test
    void test_selectCountByDepId(){
        employeeService.insertOne(insertEmp);
        int i = employeeService.selectCountByDepId(insertEmp.getDepartmentId());
        Assertions.assertNotEquals(0,i);

        // 查不存在的
        i = employeeService.selectCountByDepId(getRandId());
        Assertions.assertEquals(0,i);
    }

    @Test
    void test_selectCountByJobId(){
        employeeService.insertOne(insertEmp);
        int i = employeeService.selectCountByJobId(insertEmp.getJobTitleId());
        Assertions.assertNotEquals(0,i);

        // 查不存在的
        i = employeeService.selectCountByJobId(getRandId());
        Assertions.assertEquals(0,i);

    }

    @Test
    void test_selectOneByIdForUpdate(){
        employeeService.insertOne(insertEmp);
        Employee ret = employeeService.selectOneByIdForUpdate(insertEmp.getId());
        assertionExceptTime(insertEmp,ret);

        // 查不存在
        Employee ret2 = employeeService.selectOneByIdForUpdate((long) getRandId());
        Assertions.assertNull(ret2);

    }

    @Test
    void test_selectAll(){
        int ret = employeeService.selectAllCount(null);
        Assertions.assertEquals(11,ret);
    }

    @Test
    void test_fill(){
        Employee employee = new Employee();
        employee.setDepartmentId(RandomUtil.getRandInt());
        employee.setJobTitleId(1);
        Assertions.assertThrows(CustomException.class,()->employeeService.fillEmpInfo(employee));
    }


}