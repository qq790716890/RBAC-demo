package services;

import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.CommonUtils;
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.RandomUtil;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.DepartmentService;
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
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    private static Employee loginEmp;

    private Department insertDep;

    private static Random random;

    private static MockedStatic<EmployeeContext> employeeContextMockedStatic;


    public int getRandId(){
        return random.nextInt();
    }

    public String getRandString(){
        return CommonUtils.generateUUID().substring(0,15);
    }


    @BeforeAll
    public static void setup2(){
        random = new Random();

        // AOP 中需要 EmployeeContext 取当前登陆用户
        loginEmp = new Employee();
        loginEmp.setId(1L);
//        Mockito.mockStatic(EmployeeContext.class);
//        Mockito.when(EmployeeContext.getEmployee()).thenReturn(loginEmp);

    }

    @AfterEach
    public void after(){
        employeeContextMockedStatic.close();
    }


    @BeforeEach
    public  void setup() throws Exception {
        employeeContextMockedStatic = Mockito.mockStatic(EmployeeContext.class);
        employeeContextMockedStatic.when(EmployeeContext::getEmployee).thenReturn(loginEmp);

        insertDep = new Department();
        insertDep.setName(getRandString());
        insertDep.setRank(getRandId());
    }

    public void assertionExceptTime(Department expect, Department obt){
        // 判断插入的数据是否一致
        Assertions.assertAll("Department properties",
                () -> Assertions.assertEquals(expect.getId(), obt.getId()),
                () -> Assertions.assertEquals(expect.getName(), obt.getName()),
                () -> Assertions.assertEquals(expect.getCreateUserId(), obt.getCreateUserId()),
                () -> Assertions.assertEquals(expect.getRank(), obt.getRank()),
                () -> Assertions.assertEquals(expect.getDescription(), obt.getDescription()),
                () -> Assertions.assertEquals(expect.getUpdateUserId(), obt.getUpdateUserId())
        );
    }


    @Test
    void test_selectByPage(){
        // 查找
        List<Department> results = departmentService.selectByPage(10, 0, null);
        Assertions.assertEquals(8,results.size());

        List<Department> results2 = departmentService.selectByPage(10, 0, "部");
        Assertions.assertEquals(3,results2.size());

    }

    @Test
    void test_insertOne(){

        departmentService.insertOne(insertDep);
        Department ret = departmentService.selectOneById(insertDep.getId());
        // 判断插入的数据是否一致
        assertionExceptTime(insertDep,ret);


    }


    @Test
    void test_updateOne(){
        departmentService.insertOne(insertDep);

        insertDep.setName(getRandString());
        insertDep.setDescription(getRandString());

        departmentService.updateOne(insertDep);
        Department ret = departmentService.selectOneById(insertDep.getId());
        // 判断插入的数据是否一致
        // 判断插入的数据是否一致
        assertionExceptTime(insertDep,ret);

    }

    @Test
    void test_selectOneById(){
        departmentService.insertOne(insertDep);
        insertDep.setId(insertDep.getId());
        Department ret = departmentService.selectOneById(insertDep.getId());
        assertionExceptTime(insertDep,ret);


        // 查询不存在的id
        Department ret1 = departmentService.selectOneById(0);
        Assertions.assertNull(ret1);

    }

    @Test
    void test_selectOneByName(){

        // 查存在
        departmentService.insertOne(insertDep);
        insertDep.setId(insertDep.getId());
        Department ret = departmentService.selectOneByName(insertDep.getName());
        assertionExceptTime(insertDep,ret);


        // 查不存在
        Department ret2 = departmentService.selectOneByName("不存在的部门！！！！！！！");
        Assertions.assertNull(ret2);

    }

    @Test
    void test_updateOneByDel(){
        // 查存在
        departmentService.insertOne(insertDep);
        insertDep.setId(insertDep.getId());
        Department ret = departmentService.selectOneByName(insertDep.getName());
        assertionExceptTime(insertDep,ret);


        // 查不存在
        Department ret2 = departmentService.selectOneByName(getRandString());
        Assertions.assertNull(ret2);


    }

    @Test
    void test_selectLimitById(){
        insertDep.setRank(Integer.MIN_VALUE);
        departmentService.insertOne(insertDep);

        List<Department> d1 = departmentService.selectLimitById(insertDep.getId());
        int all = departmentService.selectAllCount(null);
        Assertions.assertEquals(all,d1.size());

        // 查询不存在的
        insertDep.setRank(Integer.MAX_VALUE);
        insertDep.setName(getRandString());
        departmentService.insertOne(insertDep);

        List<Department> d2 = departmentService.selectLimitById(insertDep.getId());
        Assertions.assertEquals(1,d2.size());

    }

    @Test
    void test_selectAll(){
        int ret = departmentService.selectAllCount(null);
        Assertions.assertEquals(8,ret);
        int ret2 = departmentService.selectAllCount(RandomUtil.getRandString());
        Assertions.assertEquals(0,ret2);
    }




}