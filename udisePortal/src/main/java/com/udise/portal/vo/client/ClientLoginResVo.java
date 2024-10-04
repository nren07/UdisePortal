package com.udise.portal.vo.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class ClientLoginResVo {
    private Long id;
    private String msg;
    private String authtoken;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public ClientLoginResVo() {
    }

    public ClientLoginResVo(String msg, String authtoken,Long id) {
        this.msg = msg;
        this.authtoken = authtoken;
        this.id=id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
