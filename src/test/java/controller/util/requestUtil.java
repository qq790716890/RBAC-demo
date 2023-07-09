package controller.util;

import com.rbac_demo.controller.advice.CustomException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;

/**
 * @author : lzy
 * @date : 2023/7/8
 * @effect :
 */

public class requestUtil {


    public static ResultActions sendRequest(MockMvc mockMvc, String url, String method, String content, ResultMatcher...  expectLs) throws Exception {
        return sendRequest(mockMvc, url, method, content, null, expectLs);
    }


    public static ResultActions sendRequest(MockMvc mockMvc, String url, String method, String content, Cookie cookie, ResultMatcher...  expectLs) throws Exception {
        MockHttpServletRequestBuilder builder = null;
        switch (method) {
            case "post":
                builder = MockMvcRequestBuilders.post(url);
                break;
            case "get":
                builder = MockMvcRequestBuilders.get(url);
                break;
            case "put":
                builder = MockMvcRequestBuilders.put(url);
                break;
            case "delete":
                builder = MockMvcRequestBuilders.delete(url);
                break;
            default:
                throw new CustomException("请求方式不支持！");
        }

        builder.content(content.getBytes()) //传json参数
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        if (cookie!=null){
            builder.cookie(cookie);
        }

        ResultActions re = mockMvc.perform(builder);

        if (expectLs!=null){
            re.andExpectAll(expectLs);
        }
        return re;
    }
}
