package test.com.pccw.interview; 

import com.alibaba.fastjson.JSONObject;
import com.pccw.interview.MainApplication;
import org.junit.Assert;
import org.junit.Test; 
import org.junit.Before; 
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Pattern;

/** 
* UserController Tester. 
* 
* @author <Authors name> 
* @since <pre>12月 12, 2020</pre> 
* @version 1.0 
*/ 
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

@Before
public void before() { 
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
} 

    /**
    * Method: create(@RequestBody User user)
    */ 
    @Test
    public void testCreate() throws Exception {
        String jsonBody = "{\"first\":\"uFirst\", \"last\":\"uLast\"," +
                "\"email\":\"test@qq.com\", \"password\":\"123456\"}";
        MvcResult mvcResult = Utils.performCreate(mockMvc, jsonBody);
        Assert.assertEquals("Request wrong", 
                200, mvcResult.getResponse().getStatus());
        JSONObject jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        String reg = "[0-9a-z-]+";
        Assert.assertTrue(Pattern.matches(reg, jsonRes.get("id").toString()));

        jsonBody = "{}";
        mvcResult = Utils.performCreate(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        
        jsonBody = "{\"last\":\"uLast\"," +
                "\"email\":\"test@qq.com\", \"password\":\"123456\"}";
        mvcResult = Utils.performCreate(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The first cannot be null.", jsonRes.get("message"));

        jsonBody = "{\"first\":\"uFirst\"," +
                "\"email\":\"test@qq.com\", \"password\":\"123456\"}";
        mvcResult = Utils.performCreate(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The last cannot be null.", jsonRes.get("message"));

        jsonBody = "{\"first\":\"uFirst\", \"last\":\"uLast\"," +
                "\"password\":\"123456\"}";
        mvcResult = Utils.performCreate(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email cannot be null.", jsonRes.get("message"));

        jsonBody = "{\"first\":\"uFirst\", \"last\":\"uLast\"," +
                "\"email\":\"test.com\", \"password\":\"123456\"}";
        mvcResult = Utils.performCreate(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email is invalid.", jsonRes.get("message"));

        jsonBody = "{\"first\":\"uFirst\", \"last\":\"uLast\"," +
                "\"email\":\"test@qq.com\"}";
        mvcResult = Utils.performCreate(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The password cannot be null.", jsonRes.get("message"));
    }

    @Test
    public void testLogin() throws Exception {
        // create a user
        String jsonBody = "{\"first\":\"uFirst\", \"last\":\"uLast\"," +
                "\"email\":\"testLogin@pccw.com\", \"password\":\"testLogin\"}";
        MvcResult mvcResult = Utils.performCreate(mockMvc, jsonBody);
        Assert.assertEquals("Request wrong",
                200, mvcResult.getResponse().getStatus());
        
        // test user login
        jsonBody = "{}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        JSONObject jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email or password is invalid.", 
                jsonRes.get("message"));
        
        jsonBody = "{\"password\":\"123456\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email or password is invalid.",
                jsonRes.get("message"));

        jsonBody = "{\"email\":\"test@qq.com\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email or password is invalid.",
                jsonRes.get("message"));

        jsonBody = "{\"email\":\"test@pccw.com\", \"password\":\"123456\"," +
                "\"noKey\":\"impact\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email or password is invalid.",
                jsonRes.get("message"));

        jsonBody = "{\"email\":\"wrong@qq.com\", \"password\":\"123456\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email or password is invalid.",
                jsonRes.get("message"));

        jsonBody = "{\"email\":\"testLogin@pccw.com\", \"password\":\"wrong\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The email or password is invalid.",
                jsonRes.get("message"));

        jsonBody = "{\"email\":\"testLogin@pccw.com\", \"password\":\"testLogin\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        String reg = "[0-9a-z-]+";
        Assert.assertTrue(Pattern.matches(reg, jsonRes.get("token").toString()));
    }
    
    @Test
    public void testGetInfo() throws Exception {
        // create a user
        String jsonBody = "{\"first\":\"uFirst\", \"last\":\"uLast\"," +
                "\"email\":\"testLogin@pccw.com\", \"password\":\"testLogin\"}";
        MvcResult mvcResult = Utils.performCreate(mockMvc, jsonBody);
        Assert.assertEquals("Request wrong",
                200, mvcResult.getResponse().getStatus());
        // login
        jsonBody = "{\"email\":\"testLogin@pccw.com\", \"password\":\"testLogin\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        JSONObject jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        String token = jsonRes.toJSONString();
        
        // test get user info
        jsonBody = "{}";
        mvcResult = Utils.performGetInfo(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));

        jsonBody = "{\"notToken\":\"any\"}";
        mvcResult = Utils.performGetInfo(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));

        jsonBody = "{\"token\":\"any\", \"noKey\":\"impact\"}";
        mvcResult = Utils.performGetInfo(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));

        jsonBody = "{\"token\":\"wrong-token\"}";
        mvcResult = Utils.performGetInfo(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));
        
        mvcResult = Utils.performGetInfo(mockMvc, token);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(4, jsonRes.size());
        Assert.assertNotNull(jsonRes.get("id"));
        Assert.assertEquals("uFirst", jsonRes.get("first"));
        Assert.assertEquals("uLast", jsonRes.get("last"));
        Assert.assertEquals("testLogin@pccw.com", jsonRes.get("email"));
    }

    @Test
    public void testLogout() throws Exception {
        // create a user
        String jsonBody = "{\"first\":\"uFirst\", \"last\":\"uLast\"," +
                "\"email\":\"testLogin@pccw.com\", \"password\":\"testLogin\"}";
        MvcResult mvcResult = Utils.performCreate(mockMvc, jsonBody);
        Assert.assertEquals("Request wrong",
                200, mvcResult.getResponse().getStatus());
        // login
        jsonBody = "{\"email\":\"testLogin@pccw.com\", \"password\":\"testLogin\"}";
        mvcResult = Utils.performLogin(mockMvc, jsonBody);
        JSONObject jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        String token = jsonRes.toJSONString();
        
        // get user info
        mvcResult = Utils.performGetInfo(mockMvc, token);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(4, jsonRes.size());
        Assert.assertNotNull(jsonRes.get("id"));
        Assert.assertEquals("uFirst", jsonRes.get("first"));
        Assert.assertEquals("uLast", jsonRes.get("last"));
        Assert.assertEquals("testLogin@pccw.com", jsonRes.get("email"));
        
        // test logout
        jsonBody = "{}";
        mvcResult = Utils.performLogout(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));
        
        jsonBody = "{\"token\":\"not-exist\"}";
        mvcResult = Utils.performLogout(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));

        jsonBody = "{\"token\":\"not-exist\", \"noKey\":\"impact\"}";
        mvcResult = Utils.performLogout(mockMvc, jsonBody);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));
        
        mvcResult = Utils.performLogout(mockMvc, token);
        mvcResult = Utils.performGetInfo(mockMvc, token);
        jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals(400, jsonRes.get("code"));
        Assert.assertEquals("The token is invalid.", jsonRes.get("message"));
    }
    
} 
