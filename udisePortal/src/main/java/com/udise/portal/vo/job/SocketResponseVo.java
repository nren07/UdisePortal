package com.udise.portal.vo.job;

// DTO for job events
public class SocketResponseVo {
    private String eventType;
    private String msg;

    public SocketResponseVo(String eventType, String msg) {
        this.eventType = eventType;
        this.msg = msg;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
