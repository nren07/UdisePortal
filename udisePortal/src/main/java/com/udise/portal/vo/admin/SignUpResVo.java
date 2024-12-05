package com.udise.portal.vo.admin;

import java.util.UUID;

public class SignUpResVo {
    private UUID id;
    private String username;
    private String password;
    private String msg;

    public SignUpResVo() {
    }

    public SignUpResVo(UUID id, String username, String password, String msg) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.msg = msg;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
