package utils;


import com.rbac_demo.common.CookieUtil;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author : lzy
 * @date : 2023/7/5
 * @effect :
 */

public class CookieUtilTest {


    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        // 创建 HttpServletRequest 的 mock 对象
        request = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    void testGetValue_NormalCase() {
        // 创建 Cookie 数组，模拟 request.getCookies() 的返回值
        Cookie[] cookies = {
                new Cookie("name1", "value1"),
                new Cookie("name2", "value2"),
                new Cookie("name3", "value3")
        };

        // 设置 request.getCookies() 方法的模拟行为
        Mockito.when(request.getCookies()).thenReturn(cookies);

        // 调用 CookieUtil.getValue() 方法进行测试
        String result = CookieUtil.getValue(request, "name2");

        // 断言验证结果是否符合预期
        Assertions.assertEquals("value2", result);
    }

    @Test
    void testGetValue_NullRequest() {
        // 调用 CookieUtil.getValue() 方法进行测试，传入 null 作为请求对象
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CookieUtil.getValue(null, "name");
        });

        // 验证异常消息是否符合预期
        String expectedMessage = "参数为空!";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

    }

    @Test
    void testGetValue_NullName() {
        // 调用 CookieUtil.getValue() 方法进行测试，传入 null 作为请求对象
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CookieUtil.getValue(request, null);
        });

        // 验证异常消息是否符合预期
        String expectedMessage = "参数为空!";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetValue_NoCookies() {
        // 设置 request.getCookies() 方法的模拟行为，返回 null
        Mockito.when(request.getCookies()).thenReturn(null);

        // 调用 CookieUtil.getValue() 方法进行测试
        String result = CookieUtil.getValue(request, "name");

        // 断言验证结果是否为 null
        Assertions.assertNull(result);
    }

    @Test
    void testGetValue_CookieNotFound() {
        // 创建一个空的 Cookie 数组，模拟 request.getCookies() 的返回值
        Cookie[] cookies = {};

        // 设置 request.getCookies() 方法的模拟行为
        Mockito.when(request.getCookies()).thenReturn(cookies);

        // 调用 CookieUtil.getValue() 方法进行测试
        String result = CookieUtil.getValue(request, "name");

        // 断言验证结果是否为 null
        Assertions.assertNull(result);
    }
}

