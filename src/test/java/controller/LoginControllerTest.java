package controller;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.*;
import com.rbac_demo.dao.LoginTicketMapper;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.LoginTicket;
import com.rbac_demo.entity.R;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.Cookie;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import java.util.HashMap;
import java.util.Map;

import static controller.util.requestUtil.sendRequest;

/**
 * @author : lzy
 * @date : 2023/7/8
 * @effect :
 */


@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String preUrl = "";

    private static KeyPair keyPair;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    private MockedStatic<EmployeeContext> employeeContextMockedStatic;


    @BeforeEach
    public void setup2() throws NoSuchAlgorithmException {
        keyPair = RSAUtils.getKeyPair();
        employeeContextMockedStatic = Mockito.mockStatic(EmployeeContext.class);
        employeeContextMockedStatic.when(EmployeeContext::getKeyPair).thenReturn(keyPair);
    }

    @AfterEach
    public void setup3() {
        employeeContextMockedStatic.close();
    }


    @Test
    @Transactional
    @Rollback()
    void login() throws Exception {
        String url = preUrl + "/login";
        String method = "post";
        // 设置 期望对象
        Employee expectedEmp = new Employee();
        expectedEmp.setUserName("admin");
        expectedEmp.setPassword("admin123");
        expectedEmp.setName("admin");

        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("userName", expectedEmp.getUserName());
        params.put("password", RSAUtils.encryptBase64(expectedEmp.getPassword(), keyPair.getPublic()));

        ResultMatcher expectStatus = MockMvcResultMatchers.status().isOk();
        ResultMatcher expectTicket = MockMvcResultMatchers.cookie().exists("ticket");

        String content = JSON.toJSONString(params);
        ResultActions resultActions = sendRequest(mockMvc, url, method, content, expectStatus, expectTicket);

        MvcResult mvcResult = resultActions.andReturn();
        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<Employee>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<Employee> retObj = JSON.parseObject(retContentString, typeReference);

        Assertions.assertEquals(1, retObj.getCode());
        Employee logInEmp = retObj.getData();
        Assertions.assertEquals(expectedEmp.getName(), logInEmp.getName());

    }


    @Test
    @Transactional
    @Rollback()
    void login_invalid() throws Exception {
        String url = preUrl + "/login";
        String method = "post";
        // 设置 期望对象

        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        // 1- 设置不存在的用户
        params.put("userName", RandomUtil.getRandString());
        params.put("password", RSAUtils.encryptBase64(RandomUtil.getRandString(), keyPair.getPublic()));

        ResultMatcher expectStatus = MockMvcResultMatchers.status().isOk();
        String content = JSON.toJSONString(params);
        ResultActions resultActions = sendRequest(mockMvc, url, method, content, expectStatus);
        MvcResult mvcResult = resultActions.andReturn();
        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        TypeReference<R<String>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<String> retObj = JSON.parseObject(retContentString, typeReference);
        String errMsg = retObj.getMsg();
        Assertions.assertEquals("用户或密码错误！", errMsg);


        // 2- 用户存在，密码错误
        params.put("userName", "admin");
        params.put("password", RSAUtils.encryptBase64(RandomUtil.getRandString(), keyPair.getPublic()));
        expectStatus = MockMvcResultMatchers.status().isOk();
        content = JSON.toJSONString(params);
        resultActions = sendRequest(mockMvc, url, method, content, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        errMsg = retObj.getMsg();
        Assertions.assertEquals("用户或密码错误！", errMsg);



        // 3- 用户被禁用
        params.put("userName", "empReadOnly");
        params.put("password", RSAUtils.encryptBase64("empReadOnly2", keyPair.getPublic()));
        expectStatus = MockMvcResultMatchers.status().isOk();
        content = JSON.toJSONString(params);
        resultActions = sendRequest(mockMvc, url, method, content, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        errMsg = retObj.getMsg();
        Assertions.assertEquals("用户已被禁用，请联系管理员！！！", errMsg);

    }


    @Test
    @Transactional
    @Rollback()
    void logout() throws Exception {
        String url = preUrl + "/logout";
        String method = "post";
        // 该 ticket 到 2030年有效 ,用户id 为 1
        String ticket = "ddaf410cc86640398b5d84188961da17";

        loginTicketMapper.updateStatus(ticket, 0);

        // 设置期望对象
        String expectData = "退出登陆成功!";

        String content = "";
        ResultActions resultActions = sendRequest(mockMvc, url, method, content, new Cookie("ticket", ticket));

        MvcResult mvcResult = resultActions.andReturn();

        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);


        // 定义泛型对象类型
        TypeReference<R<String>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<String> retObj = JSON.parseObject(retContentString, typeReference);

        Assertions.assertEquals(1, retObj.getCode());
        String data = retObj.getData();
        Assertions.assertEquals(expectData, data);

    }


//    @PostMapping("/logout")
//    public R<String> logout(@RequestBody Employee emp,@CookieValue("ticket") String ticket){
//
//    }
//

    @Test
    @Transactional
    @Rollback()
    void getPublicKey() throws Exception {
        String url = preUrl + "/getPublicKey";
        String method = "get";
        String content = "";
        ResultMatcher expectStatus = MockMvcResultMatchers.status().isOk();

        ResultActions resultActions = sendRequest(mockMvc, url, method, content, expectStatus);

        MvcResult mvcResult = resultActions.andReturn();
        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 将 JSON 字符串转换为泛型对象
        Map retObj = JSON.parseObject(retContentString, Map.class);
        Assertions.assertEquals(1, retObj.get("code"));
        Assertions.assertNotNull(retObj.get("publicKey"));
        Assertions.assertEquals("RSA", retObj.get("algorithm"));
    }


}
