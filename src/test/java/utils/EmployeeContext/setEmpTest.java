package utils.EmployeeContext;

/**
 * @author : lzy
 * @date : 2023/7/5
 * @effect :
 */
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


public class setEmpTest {

    private ServletRequestAttributes servletRequestAttributes;

    @BeforeEach
    public void setUp() {
        servletRequestAttributes = new ServletRequestAttributes(new MockHttpServletRequest());
    }

    @Test
    public void testSetEmployee() {
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        // Create an Employee object
        Employee employee = new Employee();
        employee.setName("john");
        // Call the setEmployee() method
        EmployeeContext.setEmployee(employee);

        // Assert that the employee object is set in the session
        Assertions.assertEquals(employee, EmployeeContext.getEmployee());
    }

    @Test
    public void testSetEmployeeWithNullRequestAttributes() {
        // Set null request attributes
        RequestContextHolder.setRequestAttributes(null);

        // Create an Employee object
        Employee employee = new Employee();

        // Assert that calling setEmployee() throws a CustomException
        Assertions.assertThrows(CustomException.class, () -> EmployeeContext.setEmployee(employee));
    }

    @Test
    public void testSetEmployee_SessionIsNull() {
        // 创建一个模拟的HttpServletRequest对象
        HttpServletRequest request = Mockito.spy(new MockHttpServletRequest());

        // 设置模拟的HttpServletRequest对象的getSession()方法返回null
        Mockito.doReturn(null).when(request).getSession();

        // 将模拟的HttpServletRequest对象设置为当前请求的属性
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // 执行getEmployee()方法，预期会抛出CustomException异常
        Assertions.assertThrows(CustomException.class, () -> EmployeeContext.setEmployee(new Employee()));
    }
}
