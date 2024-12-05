package com.udise.portal.service.super_admin;

import com.udise.portal.vo.client.CreditUpdateReqVo;

import java.util.UUID;

public interface SuperAdminManager {
    public void addCredit(CreditUpdateReqVo obj);

    public boolean isValidSuperUser(String token, UUID id);
}
