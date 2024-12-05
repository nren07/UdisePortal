package com.udise.portal.service.super_admin.impl;

import com.udise.portal.common.JwtUtil;
import com.udise.portal.dao.ClientDao;
import com.udise.portal.dao.SuperAdminDao;
import com.udise.portal.entity.Client;
import com.udise.portal.entity.SuperAdmin;
import com.udise.portal.service.super_admin.SuperAdminManager;
import com.udise.portal.vo.client.CreditUpdateReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class SuperAdminManagerImpl implements SuperAdminManager {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private SuperAdminDao superAdminDao;

    @Override
    public void addCredit(CreditUpdateReqVo obj) {
        Client client=clientDao.getById(obj.getClientId());
        if(client!=null){
            client.setCreditPoint(obj.getCreditPoints());
            clientDao.update(client);
        }

    }

    @Override
    public boolean isValidSuperUser(String token, UUID id) {
        String username= JwtUtil.extractUsername(token.substring(7));
        SuperAdmin admin=superAdminDao.getById(SuperAdmin.class,id);
        if(admin.getUsername().equals(username)) return true;
        return false;
    }
}
