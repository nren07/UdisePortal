package com.udise.portal.service.client.impl;

import com.udise.portal.dao.ClientDao;
import com.udise.portal.entity.Client;
import com.udise.portal.service.client.ClientManager;
import com.udise.portal.vo.client.CreditUpdateReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientManagerImpl implements ClientManager {

    @Autowired
    private ClientDao clientDao;

    @Override
    public void addCredit(CreditUpdateReqVo obj) {
        Client client=clientDao.getById(obj.getClientId());
        client.setCreditPoint(obj.getCreditPoints());
        clientDao.update(client);
    }
}
