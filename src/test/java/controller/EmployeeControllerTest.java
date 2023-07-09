package controller;

/**
 * @author : lzy
 * @date : 2023/7/7
 * @effect :
 */

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.Page;

import com.rbac_demo.common.RandomUtil;

import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Employee;
import com.rbac_demo.entity.R;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


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
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


import static controller.util.requestUtil.sendRequest;


@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
@AutoConfigureMockMvc
@Transactional
@Rollback()
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class EmployeeControllerTest {

    // 这样就不需要启动服务器了，且不需要经过网络了
    @Autowired
    private MockMvc mockMvc;

    private static String preUrl = "/employee";

    private static Cookie cookieTicket;

    @BeforeAll
    public static void setup2() throws NoSuchAlgorithmException {
        cookieTicket = new Cookie("ticket", "ddaf410cc86640398b5d84188961da17");
    }


    @Test
    public void list() throws Exception {

        String url = preUrl + "/list";
        String method = "post";
        String content = "{\"currentPage\":1,\"pageSize\":10}";
        ResultMatcher expectStatus = MockMvcResultMatchers.status().isOk();

        ResultActions resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);

        MvcResult mvcResult = resultActions.andReturn();
        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<Page<Employee>>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<Page<Employee>> retObj = JSON.parseObject(retContentString, typeReference);

        Assertions.assertEquals(10, retObj.getData().getRecords().size());


        // 0- 测试未登陆的情况
        expectStatus = MockMvcResultMatchers.status().is4xxClientError();
        resultActions = sendRequest(mockMvc, url, method, content, expectStatus);


        // 2- 传参，参数为空
        url = preUrl + "/list";
        method = "post";
        content = "{\"currentPage\":null,\"pageSize\":10}";
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);

        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        TypeReference<R<String>> typeReference2 = new TypeReference<>() {
        };
        // 将 JSON 字符串转换为泛型对象
        R<String> retObj2 = JSON.parseObject(retContentString, typeReference2);
        Assertions.assertEquals("请求参数不合法！", retObj2.getMsg());

        // 3- 传参，参数为空
        url = preUrl + "/list";
        method = "post";
        content = "{\"currentPage\":null,\"pageSize\":null}";
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);

        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        TypeReference<R<String>> typeReference3 = new TypeReference<>() {
        };
        // 将 JSON 字符串转换为泛型对象
        R<String> retObj3 = JSON.parseObject(retContentString, typeReference2);
        Assertions.assertEquals("请求参数不合法！", retObj2.getMsg());

    }


    @Test
    public void queryOne() throws Exception {
        // 1. 查询 存在的 id
        Employee expectedEmp = new Employee();
        expectedEmp.setId(7L);
        expectedEmp.setName("专门管理部门的人");

        String url = preUrl + "/query/" + expectedEmp.getId();
        String method = "get";

        ResultMatcher expectStatus = MockMvcResultMatchers.status().isOk();
        ResultActions resultActions = sendRequest(mockMvc, url, method, "", cookieTicket, expectStatus);

        MvcResult mvcResult = resultActions.andReturn();
        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<Employee>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<Employee> retObj = JSON.parseObject(retContentString, typeReference);
        Employee retEmp = retObj.getData();
        Assertions.assertEquals(expectedEmp.getName(), retEmp.getName());


        // 2. 查询 不存在的 id
        url = preUrl + "/query/" + RandomUtil.getRandInt();
        method = "get";
        String expectMsg = "需要查询的用户ID不存在！";
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, "", cookieTicket, expectStatus);

        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<String>> typeReference2 = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<String> stringR = JSON.parseObject(retContentString, typeReference2);
        Assertions.assertEquals(0, stringR.getCode());
        String errMsg = stringR.getMsg();
        Assertions.assertEquals(expectMsg, errMsg);

    }


    @Test
    public void add() throws Exception {
        String url = preUrl + "/add";
        String method = "post";
        String expectAddMsg = "新增员工成功";
        String expectUpdateMsg = "更新员工成功";

        String content = "";
        ResultMatcher expectStatus = null;
        ResultActions resultActions = null;
        MvcResult mvcResult = null;
        byte[] contentBytes = null;
        String retContentString = null;

        Employee insertEmp = new Employee();
        insertEmp.setName(RandomUtil.getRandString());
        insertEmp.setDepartmentId(1);
        insertEmp.setStatus(0);
        insertEmp.setJobTitleId(1);
        insertEmp.setUserName(RandomUtil.getRandString());
        insertEmp.setPassword(RandomUtil.getRandString());

        // 1. 新增-正常
        content = JSON.toJSONString(insertEmp);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<String>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<String> retObj = JSON.parseObject(retContentString, typeReference);
        String retMsg = retObj.getData();
        Assertions.assertEquals(expectAddMsg, retMsg);

        // 2. 新增-参数异常
        // {name: "sdasd", departmentId: 19, status: 1, jobTitleId: 7, userName: "sadsa", password: "sadsa",…}
        insertEmp.setJobTitleId(RandomUtil.getRandInt());
        insertEmp.setUserName(RandomUtil.getRandString());
        content = JSON.toJSONString(insertEmp);

        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(0, retObj.getCode());
        Assertions.assertNotNull(retObj.getMsg());

        // 3. 更新-正常
        Employee updateEmp = new Employee();
        updateEmp.setId(7L);
        updateEmp.setName(RandomUtil.getRandString());
        updateEmp.setDepartmentId(1);
        updateEmp.setStatus(0);
        updateEmp.setJobTitleId(1);
        updateEmp.setUserName(RandomUtil.getRandString());
        updateEmp.setPassword(RandomUtil.getRandString());

        content = JSON.toJSONString(updateEmp);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        typeReference = new TypeReference<>() {
        };
        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        retMsg = retObj.getData();
        Assertions.assertEquals(expectUpdateMsg, retMsg);

        // 2. 更新-参数异常
        // {name: "sdasd", departmentId: 19, status: 1, jobTitleId: 7, userName: "sadsa", password: "sadsa",…}
        updateEmp.setJobTitleId(RandomUtil.getRandInt());
        updateEmp.setUserName(RandomUtil.getRandString());
        content = JSON.toJSONString(insertEmp);

        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(0, retObj.getCode());
        Assertions.assertNotNull(retObj.getMsg());

    }

    @Test
    public void add_invalid() throws Exception {
        String url = preUrl + "/add";
        String method = "post";
        String content = "";
        ResultMatcher expectStatus = null;
        ResultActions resultActions = null;
        MvcResult mvcResult = null;
        byte[] contentBytes = null;
        String retContentString = null;

        Employee updateEmp = new Employee();
        updateEmp.setId(1L);
        updateEmp.setName(RandomUtil.getRandString());
        updateEmp.setDepartmentId(1);
        updateEmp.setStatus(0);
        updateEmp.setJobTitleId(1);
        updateEmp.setUserName(RandomUtil.getRandString());
        updateEmp.setPassword(RandomUtil.getRandString());

        // 3. 新增的员工username与其他的重复
        updateEmp.setUserName("depAdmin");
        content = JSON.toJSONString(updateEmp);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        TypeReference<R<String>> typeReference1 = new TypeReference<>() {};
        R<String> retObj = JSON.parseObject(retContentString, typeReference1);
        String retMsg = retObj.getMsg();
        Assertions.assertTrue(retMsg.contains("已存在"));


        // 4. 更新的id不存在
        updateEmp.setName(RandomUtil.getRandString());
        updateEmp.setId((long) RandomUtil.getRandInt());
        content = JSON.toJSONString(updateEmp);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        typeReference1 = new TypeReference<>() {};
        retObj = JSON.parseObject(retContentString, typeReference1);
        retMsg = retObj.getMsg();
        Assertions.assertTrue(retMsg.contains("不存在"));
    }


    @Test
    public void updateStatus() throws Exception {
        int updateId = 1;
        String url = preUrl + "/updateStatus/" + updateId;
        String method = "put";
        String expectData = "更新状态成功！";

        Map<String, Integer> requestParams = new HashMap<>();
        requestParams.put("status", 0);

        String content = "";
        ResultMatcher expectStatus = null;
        ResultActions resultActions = null;
        MvcResult mvcResult = null;
        byte[] contentBytes = null;
        String retContentString = null;


        content = JSON.toJSONString(requestParams);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<String>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<String> retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(expectData, retObj.getData());


        // 更新id參數类型不正确
        String url2 = preUrl + "/updateStatus/" + RandomUtil.getRandString();

        content = JSON.toJSONString(requestParams);
        expectStatus = MockMvcResultMatchers.status().is4xxClientError();
        resultActions = sendRequest(mockMvc, url2, method, content, cookieTicket, expectStatus);


        // 更新id不存在
        String url3 = preUrl + "/updateStatus/" + RandomUtil.getRandInt();
        content = JSON.toJSONString(requestParams);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url3, method, content, cookieTicket, expectStatus);

        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        typeReference = new TypeReference<>() {
        };
        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(0, retObj.getCode());
        Assertions.assertNotNull(expectData, retObj.getMsg());

    }


    @Test
    public void deleteOneById() throws Exception {

        // 1. 删除成功
        long deleteId = 19;
        String url = preUrl + "/delete/" + deleteId;
        String method = "delete";
        String expectData = "删除成功!";

        String content = "";
        ResultMatcher expectStatus = null;
        ResultActions resultActions = null;
        MvcResult mvcResult = null;
        byte[] contentBytes = null;
        String retContentString = null;
        content = "";
        expectStatus = MockMvcResultMatchers.status().isOk();

        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        TypeReference<R<String>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<String> retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(expectData, retObj.getData());



        // 2。删除的emp ID 不存在
        String url3 = preUrl + "/delete/" + RandomUtil.getRandInt();
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url3, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertNotNull(retObj.getMsg());

    }


//}


}