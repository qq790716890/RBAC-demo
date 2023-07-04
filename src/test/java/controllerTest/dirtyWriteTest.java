package controllerTest;

import com.rbac_demo.RbacApplication;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * @author : lzy
 * @date : 2023/7/4
 * @effect :
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
public class dirtyWriteTest {
    @Autowired
    private EmployeeService employeeService;


    @Test
    public void te1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    te2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    te2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Test
    public void te2() throws InterruptedException {
        // 要更新的对象
        Employee emp = employeeService.selectOneById(19L);
        // 查询数据库的对象
        employeeService.updateOne(emp);
    }


    @Test
    public void te3() throws ExecutionException, InterruptedException {
        // 要更新的对象
        Employee emp = employeeService.selectOneById(19L);
        // 查询数据库的对象
        employeeService.updateOne(emp);
    }



}



