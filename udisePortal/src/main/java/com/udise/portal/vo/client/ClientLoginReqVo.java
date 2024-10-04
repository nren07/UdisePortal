package com.udise.portal.vo.client;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

public class ClientLoginReqVo{
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClientLoginReqVo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
