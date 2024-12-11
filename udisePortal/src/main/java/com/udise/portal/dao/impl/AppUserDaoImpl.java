package com.udise.portal.dao.impl;
import com.udise.portal.dao.AppUserDao;
import com.udise.portal.dao.ClientDao;
import com.udise.portal.entity.AppUser;
import com.udise.portal.entity.Client;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



@Repository
@Transactional
public class AppUserDaoImpl extends AbstractDaoImpl implements AppUserDao {

    @Autowired
    private ClientDao clientDao;

    private static final Logger log = LoggerFactory.getLogger(AppUserDaoImpl.class);

    @Override
    public AppUser findByEmail(String email) {
        try{
            TypedQuery<AppUser> query = getCurrentSession().createQuery(
                    "SELECT u FROM AppUser u WHERE u.email = :email", AppUser.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public AppUser findByUserName(String userName) {
        try{
            TypedQuery<AppUser> query = getCurrentSession().createQuery(
                    "SELECT u FROM AppUser u WHERE u.username = :username", AppUser.class);
            query.setParameter("username", userName);
            return query.getSingleResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public AppUser getById(Long id) {
        return getCurrentSession().find(AppUser.class, id);
    }

    @Override
    public AppUser findOldPassByEmail(String oldPassword) {
        return null;
    }

    @Override
    public List<AppUser> findByRoleId(Long roleId) {
        try{
            TypedQuery<AppUser> query = getCurrentSession().createQuery(
                    "SELECT u FROM AppUser u WHERE u.role.id = :roleId", AppUser.class);
            query.setParameter("roleId", roleId);
            return query.getResultList();
        }catch (Exception e){

            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public AppUser getUserByUserName(String email) {
        try{
            TypedQuery<AppUser> query = getCurrentSession().createQuery(
                    "SELECT u FROM AppUser u WHERE u.email = :email", AppUser.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<AppUser> getUserList(Long clientId) {
        TypedQuery<AppUser> query = getCurrentSession().createQuery(
                "SELECT u FROM AppUser u WHERE u.client.id = :clientId", AppUser.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }

    public List<AppUser> findByRole(String role) {
        TypedQuery<AppUser> query = getCurrentSession().createQuery(
                "SELECT u FROM AppUser u WHERE u.role = :role", AppUser.class);
        query.setParameter("role", role);
        return query.getResultList();
    }


}
