package utils.EmployeeContext;

/**
 * @author : lzy
 * @date : 2023/7/6
 * @effect :
 */
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.RSAUtils;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.when;

public class clearTest {

    private ServletRequestAttributes requestAttributes;

    @BeforeEach
    public void setUp() {
        requestAttributes = new ServletRequestAttributes(new MockHttpServletRequest());
    }

    @Test
    public void testClear() {
        // Create a mock HttpServletRequest and HttpSession
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = new MockHttpSession();
        request.setSession(session);

        // Set the mock request attributes
        RequestContextHolder.setRequestAttributes(requestAttributes);

        // Set some attributes in the session
        Employee employee = new Employee();
        employee.setName("JOHN");
        session.setAttribute("employee", employee);
        try {
            session.setAttribute("keyPair", RSAUtils.getKeyPair());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Call the clear() method
        EmployeeContext.clear();

        // Assert that the attributes are removed from the session
        Assertions.assertNull(EmployeeContext.getEmployee());
        Assertions.assertNull(EmployeeContext.getKeyPair());
    }

    @Test
    public void testClearWithNullRequestAttributes() {
        // Set null request attributes
        RequestContextHolder.setRequestAttributes(null);

        // Assert that calling clear() throws a CustomException
        Assertions.assertThrows(CustomException.class, EmployeeContext::clear);
    }

    @Test
    public void testClearWithNullSession() {
        // Set null request attributes
        // 创建一个模拟的HttpServletRequest对象
        HttpServletRequest request = Mockito.spy(new MockHttpServletRequest());

        // 设置模拟的HttpServletRequest对象的getSession()方法返回null
        Mockito.doReturn(null).when(request).getSession();

        // 将模拟的HttpServletRequest对象设置为当前请求的属性
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Assert that calling clear() throws a CustomException
        Assertions.assertThrows(CustomException.class, EmployeeContext::clear);
    }

}
