package controller;

/**
 * @author : lzy
 * @date : 2023/7/7
 * @effect :
 */

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.Page;
import com.rbac_demo.common.RandomUtil;
import com.rbac_demo.controller.advice.CustomException;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.Department;
import com.rbac_demo.entity.R;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson2.JSONWriter.Feature.BrowserCompatible;
import static controller.util.requestUtil.sendRequest;


@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
@AutoConfigureMockMvc
@Transactional
@Rollback()
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class DepartmentControllerTest {

    // 这样就不需要启动服务器了，且不需要经过网络了
    @Autowired
    private MockMvc mockMvc;

    private static String preUrl = "/department";

    private Cookie cookieTicket;

    @BeforeEach
    public void setup2() throws NoSuchAlgorithmException {
        cookieTicket = new Cookie("ticket", "ddaf410cc86640398b5d84188961da17");
    }


    @Test
    public void list() throws Exception {

        // 1- 正常传参
        String url = preUrl + "/list";
        String method = "post";
        String content = "{\"currentPage\":1,\"pageSize\":10}";
        ResultMatcher expectStatus = MockMvcResultMatchers.status().isOk();

        ResultActions resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);

        MvcResult mvcResult = resultActions.andReturn();
        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<Page<Department>>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<Page<Department>> retObj = JSON.parseObject(retContentString, typeReference);

        Assertions.assertEquals(8, retObj.getData().getRecords().size());





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

        Assertions.assertEquals("请检查请求参数是否正确!", retObj2.getMsg());

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
        Assertions.assertEquals("请检查请求参数是否正确!", retObj2.getMsg());

    }


    @Test
    public void queryOneById() throws Exception {
        // 1. 查询 存在的 id
        Department expectObj = new Department();
        expectObj.setId(1);
        expectObj.setDescription("最高部门");

        String url = preUrl + "/query/" + expectObj.getId();
        String method = "get";

        ResultMatcher expectStatus = MockMvcResultMatchers.status().isOk();
        ResultActions resultActions = sendRequest(mockMvc, url, method, "", cookieTicket, expectStatus);

        MvcResult mvcResult = resultActions.andReturn();
        byte[] contentBytes = mvcResult.getResponse().getContentAsByteArray();
        String retContentString = new String(contentBytes, StandardCharsets.UTF_8);

        // 定义泛型对象类型
        TypeReference<R<Department>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<Department> retR = JSON.parseObject(retContentString, typeReference);
        Department retObj = retR.getData();
        Assertions.assertEquals(expectObj.getDescription(), retObj.getDescription());


        // 2. 查询 不存在的 id
        url = preUrl + "/query/" + RandomUtil.getRandInt();
        method = "get";
        String expectMsg = "需要查询的部门ID不存在！";
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
        String expectAddMsg = "新增部门成功!";
        String expectUpdateMsg = "更新部门成功!";

        String content = "";
        ResultMatcher expectStatus = null;
        ResultActions resultActions = null;
        MvcResult mvcResult = null;
        byte[] contentBytes = null;
        String retContentString = null;

        Department insertObj = new Department();
        insertObj.setName(RandomUtil.getRandString());
        insertObj.setDescription(RandomUtil.getRandString());
        insertObj.setRank(100);

        // 0- 测试未登陆的情况
        expectStatus = MockMvcResultMatchers.status().is4xxClientError();
        resultActions = sendRequest(mockMvc, url, method, content, expectStatus);

        // 1. 新增-正常
        content = JSONObject.toJSONString(insertObj);
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


        // 2. 更新-正常
        insertObj = new Department();
        insertObj.setId(1);
        insertObj.setName(RandomUtil.getRandString());
        insertObj.setDescription(RandomUtil.getRandString());
        insertObj.setRank(RandomUtil.getRandInt());

        content = JSON.toJSONString(insertObj);
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

        Department insertObj = new Department();
        insertObj.setId(1);
        insertObj.setName(RandomUtil.getRandString());
        insertObj.setDescription(RandomUtil.getRandString());
        insertObj.setRank(100);

        // 3. 更新的部门name与其他name重复
        insertObj.setName("一部");
        content = JSON.toJSONString(insertObj);
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
        insertObj.setName(RandomUtil.getRandString());
        insertObj.setId(RandomUtil.getRandInt());
        content = JSON.toJSONString(insertObj);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        typeReference1 = new TypeReference<>() {};
        retObj = JSON.parseObject(retContentString, typeReference1);
        retMsg = retObj.getMsg();
        Assertions.assertTrue(retMsg.contains("不存在"));

        // 5. 更新对象被改了
        insertObj.setName(RandomUtil.getRandString());
        insertObj.setId(22);
        content = JSON.toJSONString(insertObj);
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        typeReference1 = new TypeReference<>() {};
        retObj = JSON.parseObject(retContentString, typeReference1);
        retMsg = retObj.getMsg();
        Assertions.assertTrue( retMsg.contains("已经被更新"));
    }

    @Test
    public void deleteOneById() throws Exception {

        // 1. 删除成功,没有人在该部门的情况
        long deleteId = 26;
        String url = preUrl + "/delete/" + deleteId;
        String method = "delete";
        String expectData = "删除成功!";
        String expectErrData = "不能删除该部门，还有员工在该部门中！";

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



        // 2. 删除失败,有人在该部门的情况
        deleteId = 19;
        String url2 = preUrl + "/delete/" + deleteId;
        content = "";

        resultActions = sendRequest(mockMvc, url2, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(expectErrData, retObj.getMsg());


        // 3。删除的emp ID 类型不正确

        String url3 = preUrl + "/delete/" + RandomUtil.getRandString();
        expectStatus = MockMvcResultMatchers.status().is4xxClientError();
        sendRequest(mockMvc, url3, method, content, cookieTicket, expectStatus);


        // 4。删除的emp ID 不存在
        String url4 = preUrl + "/delete/" + -1;
        expectStatus = MockMvcResultMatchers.status().isOk();
        resultActions = sendRequest(mockMvc, url4, method, content, cookieTicket, expectStatus);
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



    @Test
    public void queryLimitDepartment() throws Exception {

        // 1. 查询成功
        long id = 1;
        String url = preUrl + "/queryLimitDepartment/" + id;
        String method = "get";

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
        TypeReference<R<List<Department>>> typeReference = new TypeReference<>() {
        };

        // 将 JSON 字符串转换为泛型对象
        R<List<Department>> retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(8, retObj.getData().size());



        // 2. 查询失败，不存在的id
        id = RandomUtil.getRandInt();
        String url2 = preUrl + "/queryLimitDepartment/" + id;
        content = "";

        resultActions = sendRequest(mockMvc, url2, method, content, cookieTicket, expectStatus);
        mvcResult = resultActions.andReturn();
        contentBytes = mvcResult.getResponse().getContentAsByteArray();
        retContentString = new String(contentBytes, StandardCharsets.UTF_8);
        // 定义泛型对象类型
        // 将 JSON 字符串转换为泛型对象
        retObj = JSON.parseObject(retContentString, typeReference);
        Assertions.assertEquals(0,retObj.getCode());
        Assertions.assertNotNull(retObj.getMsg());

    }










}