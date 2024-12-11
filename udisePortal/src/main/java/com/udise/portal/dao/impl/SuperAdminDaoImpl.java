package com.udise.portal.dao.impl;

import com.udise.portal.dao.AppUserDao;
import com.udise.portal.dao.ClientDao;
import com.udise.portal.dao.SuperAdminDao;
import com.udise.portal.entity.AppUser;
import com.udise.portal.entity.SuperAdmin;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository

public class SuperAdminDaoImpl extends AbstractDaoImpl implements SuperAdminDao {

    @Override
    @Transactional
    public SuperAdmin findByEmail(String username) {
        try{
            TypedQuery<SuperAdmin> query = getCurrentSession().createQuery(
                    "SELECT u FROM SuperAdmin u WHERE u.username = :username", SuperAdmin.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
}
