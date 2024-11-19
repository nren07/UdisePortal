package com.udise.portal.vo.job;


public class JobStartResponseVo {
    private Integer vncPort;
    private String msg;

    public JobStartResponseVo() {
    }

    public JobStartResponseVo(Integer vncPort, String msg) {
        this.vncPort = vncPort;
        this.msg = msg;

    }

    public Integer getVncPort() {
        return vncPort;
    }

    public void setVncPort(Integer vncPort) {
        this.vncPort = vncPort;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
