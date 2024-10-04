package com.udise.portal.vo.client;

public class ClientSignUpResVo {
    private Long id;
    private String email;
    private String password;
    private String msg;

    @Override
    public String toString() {
        return "SchoolResVo{" +
                "email='" + email + '\'' +
                ", pass='" + password + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return password;
    }

    public void setPass(String pass) {
        this.password = pass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ClientSignUpResVo() {
    }

    public ClientSignUpResVo(Long id,String email, String pass, String msg) {
        this.id=id;
        this.email = email;
        this.password = pass;
        this.msg = msg;
    }
}
