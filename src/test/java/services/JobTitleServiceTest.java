package services;

import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.CommonUtils;
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.RandomUtil;
import com.rbac_demo.entity.JobTitle;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.JobTitleService;

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
public class JobTitleServiceTest{

    @Autowired
    private JobTitleService jobTitleService;

    private static Employee loginEmp;

    private JobTitle insertOne;

    private static Random random;

    public int getRandId() {
        return random.nextInt();
    }

    public String getRandString() {
        return CommonUtils.generateUUID().substring(0, 15);
    }

    private static MockedStatic<EmployeeContext> employeeContextMockedStatic;


    @BeforeAll
    public static void setup2() {
        random = new Random();

        // AOP 中需要 EmployeeContext 取当前登陆用户
        loginEmp = new Employee();
        loginEmp.setId(1L);

    }

    @AfterEach
    public void after(){
        employeeContextMockedStatic.close();
    }


    @BeforeEach
    public void setup() throws Exception {
        employeeContextMockedStatic = Mockito.mockStatic(EmployeeContext.class);
        employeeContextMockedStatic.when(EmployeeContext::getEmployee).thenReturn(loginEmp);

        insertOne = new JobTitle();
        insertOne.setName(getRandString());
        insertOne.setRank(getRandId());
    }

    public void assertionExceptTime(JobTitle expect, JobTitle obt) {
        // 判断插入的数据是否一致
        Assertions.assertAll("JobTitle properties",
                () -> Assertions.assertEquals(expect.getId(), obt.getId()),
                () -> Assertions.assertEquals(expect.getName(), obt.getName()),
                () -> Assertions.assertEquals(expect.getCreateUserId(), obt.getCreateUserId()),
                () -> Assertions.assertEquals(expect.getRank(), obt.getRank()),
                () -> Assertions.assertEquals(expect.getDescription(), obt.getDescription()),
                () -> Assertions.assertEquals(expect.getUpdateUserId(), obt.getUpdateUserId()),
                () -> Assertions.assertEquals(expect.getPermissions(), obt.getPermissions())
        );
    }


    @Test
    void test_selectByPage() {
        // 查找
        List<JobTitle> results = jobTitleService.selectByPage(10, 0, null);
        Assertions.assertEquals(10, results.size());

        List<JobTitle> results2 = jobTitleService.selectByPage(10, 0, "管理");
        Assertions.assertEquals(4, results2.size());

    }

    @Test
    void test_insertOne() {

        jobTitleService.insertOne(insertOne);
        JobTitle ret = jobTitleService.selectOneById(insertOne.getId());
        // 判断插入的数据是否一致
        assertionExceptTime(insertOne, ret);
    }


    @Test
    void test_updateOne() {
        jobTitleService.insertOne(insertOne);

        insertOne.setName(getRandString());
        insertOne.setDescription(getRandString());

        jobTitleService.updateOne(insertOne);
        JobTitle ret = jobTitleService.selectOneById(insertOne.getId());
        // 判断插入的数据是否一致
        // 判断插入的数据是否一致
        assertionExceptTime(insertOne, ret);

    }

    @Test
    void test_selectOneById() {
        jobTitleService.insertOne(insertOne);
        insertOne.setId(insertOne.getId());
        JobTitle ret = jobTitleService.selectOneById(insertOne.getId());
        assertionExceptTime(insertOne, ret);


        // 查询不存在的id
        JobTitle ret1 = jobTitleService.selectOneById(0);
        Assertions.assertNull(ret1);

    }

    @Test
    void test_selectOneByName() {

        // 查存在
        jobTitleService.insertOne(insertOne);
        insertOne.setId(insertOne.getId());
        JobTitle ret = jobTitleService.selectOneByName(insertOne.getName());
        assertionExceptTime(insertOne, ret);


        // 查不存在
        JobTitle ret2 = jobTitleService.selectOneByName("不存在的部门！！！！！！！");
        Assertions.assertNull(ret2);

    }

    @Test
    void test_updateOneByDel() {
        // 查存在
        jobTitleService.insertOne(insertOne);
        insertOne.setId(insertOne.getId());
        JobTitle ret = jobTitleService.selectOneByName(insertOne.getName());
        assertionExceptTime(insertOne, ret);


        // 查不存在
        JobTitle ret2 = jobTitleService.selectOneByName(getRandString());
        Assertions.assertNull(ret2);


    }

    @Test
    void test_selectLimitById() {
        insertOne.setRank(Integer.MIN_VALUE);
        jobTitleService.insertOne(insertOne);

        List<JobTitle> d1 = jobTitleService.selectLimitById(insertOne.getId());
        int all = jobTitleService.selectAllCount(null);
        Assertions.assertEquals(all, d1.size());

        // 查询不存在的
        insertOne.setRank(Integer.MAX_VALUE);
        insertOne.setName(getRandString());
        jobTitleService.insertOne(insertOne);

        List<JobTitle> d2 = jobTitleService.selectLimitById(insertOne.getId());
        Assertions.assertEquals(1, d2.size());

    }

    @Test
    void test_selectAllCount() {
        jobTitleService.insertOne(insertOne);
        JobTitle ret = jobTitleService.selectOneByIdForUpdate(insertOne.getId());
        assertionExceptTime(insertOne, ret);

        // 查不存在
        JobTitle ret2 = jobTitleService.selectOneByIdForUpdate(getRandId());
        Assertions.assertNull(ret2);
    }


    @Test
    void test_selectAll(){
        int ret = jobTitleService.selectAllCount(null);
        Assertions.assertEquals(14,ret);
        int ret2 = jobTitleService.selectAllCount(RandomUtil.getRandString());
        Assertions.assertEquals(0,ret2);
    }

}