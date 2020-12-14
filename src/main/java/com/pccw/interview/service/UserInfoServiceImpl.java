package com.pccw.interview.service;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.pccw.interview.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    // save the users' information
    private Map<String, User> dataMap = new HashMap<>();
    
    @Autowired
    @Resource
    Cache<String, String> cache;
    
    public void addUser(User user) {
        dataMap.put(user.getEmail(), user);
    }
    
    public User getUserByEmail(String email) {
        return dataMap.get(email);
    }
    
    public User getUserByToken(String token) {
        String email;
        if ((email = cache.asMap().get(token)) == null)
            return null;
        return dataMap.get(email);
    }
    
    public void cacheToken(String token, String email) {
        cache.put(token, email);
    }
    
    public void deleteToken(String token) {
        cache.asMap().remove(token);
    }
    
    public void printDebugInfo() {
        StringBuilder builder = new StringBuilder();
        Set<String> emails = new HashSet<>();
        for(String token: cache.asMap().keySet()) {
            emails.add(cache.asMap().get(token));
        }
        builder.append("user list:\n");
        for(User u: dataMap.values()) {
            builder.append(((JSONObject)JSONObject.toJSON(u)).toJSONString());
            if (emails.contains(u.getEmail())) {
                builder.append("isTokened");
            }
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }
}
