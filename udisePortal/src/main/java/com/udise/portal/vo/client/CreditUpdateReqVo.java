package com.udise.portal.vo.client;

public class CreditUpdateReqVo {
    private Long clientId;
    private Long creditPoints;

    public CreditUpdateReqVo() {
    }

    public CreditUpdateReqVo(Long creditPoints, Long clientId) {
        this.creditPoints = creditPoints;
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(Long creditPoints) {
        this.creditPoints = creditPoints;
    }
}
