package com.udise.portal.dao.impl;

import com.udise.portal.entity.Client;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.udise.portal.dao.ClientDao;

import java.util.List;

@Repository
@Transactional
public class ClientDaoImpl extends AbstractDaoImpl implements ClientDao {

    private static final Logger log = LoggerFactory.getLogger(AppUserDaoImpl.class);

    @Override
    public Client findByMobileAndEmail(String mobile, String email) {
        return null;
    }
    @Override
    public Client findByEmail(String email) {
        try{
            TypedQuery<Client> query = getEm().createQuery(
                    "SELECT u FROM Client u WHERE u.email = :email", Client.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Client findByUserName(String userName) {
        return null;
    }


    @Override
    public Client findOldPassByEmail(String oldPassword) {
        return null;
    }

    @Override
    public List<Client> findByModuleName(String module) {
        return List.of();
    }

    @Override
    public List<Client> findByModuleId(Long moduleId) {
        return List.of();
    }

    @Override
    public List<Client> findByRoleId(Long roleId) {
        return List.of();
    }
}
