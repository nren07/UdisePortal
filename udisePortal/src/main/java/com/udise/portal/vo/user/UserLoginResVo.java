package com.udise.portal.vo.user;

import com.udise.portal.enums.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class UserLoginResVo {
    private Long id;
    private String msg;
    private String name;
    private String authtoken;
    private Role role;

    public UserLoginResVo() {
        this.id=null;
        this.msg=null;
        this.name=null;
        this.authtoken=null;
        this.role=null;
    }

    public UserLoginResVo(Long id, String msg, String name, String authtoken, Role role) {
        this.id = id;
        this.msg = msg;
        this.name = name;
        this.authtoken = authtoken;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
