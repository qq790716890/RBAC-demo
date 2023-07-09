package utils.EmployeeContext;
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @author : lzy
 * @date : 2023/7/5
 * @effect :
 */


public class getEmpTest {

    private ServletRequestAttributes servletRequestAttributes;

    @BeforeEach
    public void setUp() {
        servletRequestAttributes = new ServletRequestAttributes(new MockHttpServletRequest());
    }

    @Test
    public void testGetEmployee() {
        // Create a mock HttpServletRequest and HttpSession
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = new MockHttpSession();
        // Create an Employee object and set it in the session
        Employee employee = new Employee();
        session.setAttribute("employee", employee);

        request.setSession(session);

        // Set the mock request attributes
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Call the getEmployee() method
        Employee result = EmployeeContext.getEmployee();

        // Assert that the returned employee object is the same as the one in the session
        Assertions.assertEquals(employee, result);
    }

    @Test
    public void testGetEmployeeWithNullRequestAttributes() {
        // Set null request attributes
        RequestContextHolder.setRequestAttributes(null);
        // Assert that calling getEmployee() throws a CustomException
        Assertions.assertThrows(CustomException.class, EmployeeContext::getEmployee);
    }

    @Test
    public void testGetEmployee_SessionIsNull() {
        // 创建一个模拟的HttpServletRequest对象
        HttpServletRequest request = Mockito.spy(new MockHttpServletRequest());

        // 设置模拟的HttpServletRequest对象的getSession()方法返回null
        Mockito.doReturn(null).when(request).getSession();

        // 将模拟的HttpServletRequest对象设置为当前请求的属性
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // 执行getEmployee()方法，预期会抛出CustomException异常
        Assertions.assertThrows(CustomException.class, EmployeeContext::getEmployee);
    }


}
