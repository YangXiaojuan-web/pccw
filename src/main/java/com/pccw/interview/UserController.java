package com.pccw.interview;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.pccw.interview.bean.User;
import com.pccw.interview.service.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserInfoServiceImpl userInfoService;
    
    @ResponseBody
    @RequestMapping(value="/user", method=RequestMethod.POST, 
            produces = "application/json;charset=UTF-8")
    public JSONObject create(@RequestBody @Validated(Default.class) User user, 
                             BindingResult bindingResult) {
        JSONObject res = new JSONObject();
        if (bindingResult.hasErrors()) {
            res.put("code", 400);
            res.put("message", bindingResult.getFieldError().getDefaultMessage());
        } else {
            userInfoService.addUser(user);
            res.put("id", user.getId());
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value="/user/login", method= RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public JSONObject login(@RequestBody JSONObject param) {
        JSONObject res = new JSONObject();
        String email;
        User user;
        if (param.size() != 2
                || param.get("email") == null
                || (email = param.get("email").toString()).equals("")
                || param.get("password") == null
                || (user = userInfoService.getUserByEmail(email)) == null
                || ! param.get("password").equals(user.getPassword())) {
            res.put("code", 400);
            res.put("message", "The email or password is invalid.");
        } else {
            String token = UUID.randomUUID().toString();
            userInfoService.cacheToken(token, user.getEmail());
            res.put("token", token);
        }
        return res;
    }

    @RequestMapping(value="/user/logout", method=RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public String logout(@RequestBody JSONObject param) {
        String token;
        if (param.size() != 1
                || param.get("token") == null
                || (token = param.get("token").toString()).equals("")
                || userInfoService.getUserByToken(token) == null ) {
            JSONObject res = new JSONObject();
            res.put("code", 400);
            res.put("message", "The token is invalid.");
            return res.toJSONString();
        }
        userInfoService.deleteToken(token);
        return null;
    }
    
    @ResponseBody
    @RequestMapping(value="/user", method = RequestMethod.GET, 
            produces = "application/json;charset=UTF-8")
    public String getInfo(@RequestBody JSONObject param) {
        User user;
        String token;
        JSONObject res = new JSONObject();
        if (param.size() != 1 
                || param.get("token") == null 
                || (token = param.get("token").toString()).equals("")
                || (user = userInfoService.getUserByToken(token)) == null ) {
            res.put("code", 400);
            res.put("message", "The token is invalid.");
            return res.toJSONString();
        }
        return JSON.toJSONString(user);
    }
}
