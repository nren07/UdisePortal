package com.udise.portal.vo.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class UserSignUpResVo {
    private String email;
    private String pass;
    private String msg;
    public UserSignUpResVo(){

    }

    public UserSignUpResVo(String email, String password, String msg) {
        this.email=email;
        this.pass=password;
        this.msg=msg;
    }

    @Override
    public String toString() {
        return "SchoolResVo{" +
                "email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
