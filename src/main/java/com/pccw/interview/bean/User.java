package com.pccw.interview.bean;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class User {
    @JSONField(ordinal = 1)
    private String id;
    @JSONField(ordinal = 2)
    @NotBlank(message = "The first cannot be null.")
    private String first;
    @JSONField(ordinal = 3)
    @NotBlank(message = "The last cannot be null.")
    private String last;
    @JSONField(ordinal = 4)
    @NotBlank(message = "The email cannot be null.")
    @Email(message = "The email is invalid.")
    private String email;
    @NotBlank(message = "The password cannot be null.")
    @JSONField(serialize = false)
    private String password;
    
    public User() {
        id = UUID.randomUUID().toString();
    }
    
    public User(String first, String last, String email, String password) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.password = password;
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public String getPassword() {
        return password;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
