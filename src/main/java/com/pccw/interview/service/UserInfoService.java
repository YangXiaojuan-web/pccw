package com.pccw.interview.service;

import com.pccw.interview.bean.User;

public interface UserInfoService {
    void addUser(User user);
    User getUserByEmail(String email);
    User getUserByToken(String token);
    void cacheToken(String token, String email);
    void deleteToken(String token);
    void printDebugInfo();
}
