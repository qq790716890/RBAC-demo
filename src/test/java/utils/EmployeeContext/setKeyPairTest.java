package utils.EmployeeContext;

/**
 * @author : lzy
 * @date : 2023/7/6
 * @effect :
 */
import com.rbac_demo.common.EmployeeContext;
import com.rbac_demo.common.RSAUtils;
import com.rbac_demo.controller.advice.CustomException;
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
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;


public class setKeyPairTest {

    private ServletRequestAttributes requestAttributes;

    @BeforeEach
    public void setUp() {
        requestAttributes = new ServletRequestAttributes(new MockHttpServletRequest());
    }

    @Test
    public void testSetKeyPair() {
        // Create a mock HttpServletRequest and HttpSession
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = new MockHttpSession();
        request.setSession(session);

        // Set the mock request attributes
        RequestContextHolder.setRequestAttributes(requestAttributes);

        // Create a KeyPair object
        KeyPair keyPair = null;
        try {
            keyPair = RSAUtils.getKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // Call the setKeyPair() method
        EmployeeContext.setKeyPair(keyPair);

        // Assert that the keyPair object is set in the session
        Assertions.assertEquals(keyPair, EmployeeContext.getKeyPair());
    }

    @Test
    public void testSetKeyPairWithNullRequestAttributes() {
        // Set null request attributes
        RequestContextHolder.setRequestAttributes(null);

        // Create a KeyPair object
        KeyPair keyPair = new KeyPair(null, null);

        // Assert that calling setKeyPair() throws a CustomException
        Assertions.assertThrows(CustomException.class, () -> EmployeeContext.setKeyPair(keyPair));
    }

    @Test
    public void testSetKeyPair_SessionIsNull() {
        // 创建一个模拟的HttpServletRequest对象
        HttpServletRequest request = Mockito.spy(new MockHttpServletRequest());

        // 设置模拟的HttpServletRequest对象的getSession()方法返回null
        Mockito.doReturn(null).when(request).getSession();

        // 将模拟的HttpServletRequest对象设置为当前请求的属性
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // 执行getEmployee()方法，预期会抛出CustomException异常
        Assertions.assertThrows(CustomException.class, () -> EmployeeContext.setKeyPair(new KeyPair(null,null)));
    }
}
