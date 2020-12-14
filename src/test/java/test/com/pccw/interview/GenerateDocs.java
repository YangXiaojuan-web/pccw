package test.com.pccw.interview;

import com.alibaba.fastjson.JSONObject;
import com.pccw.interview.MainApplication;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class GenerateDocs {
    @Rule
    public JUnitRestDocumentation restDocumentation = 
            new JUnitRestDocumentation("target/generated-snippets");
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}"))
                .build();
    }
    
    @Test
    public void heartbeatDocs() throws Exception {
        this.mockMvc.perform(get("/heartbeat"))
                .andExpect(status().isOk())
                .andDo(document("heartbeat", 
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void getDocs() throws Exception {
        this.mockMvc.perform(get("/docs"))
                .andExpect(status().isOk())
                .andDo(document("getDocs"));
    }

    @Test
    public void userDocs() throws Exception {
        this.mockMvc.perform(post("/user")
                .content("{\"first\":\"uFirst\", \"last\":\"uLast\", " +
                        "\"email\":\"test@pccw.com\", \"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("createUser", 
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

        MvcResult mvcResult  = mockMvc.perform(post("/user/login")
                .content("{\"email\":\"test@pccw.com\", \"password\":\"123456\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("userLogin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
        JSONObject jsonRes = JSONObject
                .parseObject(mvcResult.getResponse().getContentAsString());
        String token = jsonRes.toJSONString();

        this.mockMvc.perform(get("/user")
                .content(token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("getUserInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

        this.mockMvc.perform(post("/user/logout")
                .content(token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("userLogout",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
