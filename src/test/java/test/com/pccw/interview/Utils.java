package test.com.pccw.interview;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class Utils {
    public static MvcResult performCreate(MockMvc mockMvc, String jsonBody) 
            throws Exception{
        return mockMvc.perform(
                MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
        ).andReturn();
    }
    
    public static MvcResult performLogin(MockMvc mockMvc, String jsonBody) 
            throws Exception{
        return mockMvc.perform(
                MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
        ).andReturn();
    }
    
    public static MvcResult performLogout(MockMvc mockMvc, String jsonBody) 
            throws Exception{
        return mockMvc.perform(
                MockMvcRequestBuilders.post("/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
        ).andReturn();
    }

    public static MvcResult performGetInfo(MockMvc mockMvc, String jsonBody) 
            throws Exception{
        return mockMvc.perform(
                MockMvcRequestBuilders.get("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
        ).andReturn();
    }
}
