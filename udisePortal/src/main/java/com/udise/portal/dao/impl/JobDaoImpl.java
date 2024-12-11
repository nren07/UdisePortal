package com.udise.portal.dao.impl;
import com.udise.portal.dao.JobDao;
import com.udise.portal.entity.Job;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class JobDaoImpl extends AbstractDaoImpl implements JobDao {
    private static final Logger log = LoggerFactory.getLogger(AppUserDaoImpl.class);
    @Override
    public List<Job> getJobs(Long userId) {
        try{
            TypedQuery<Job> query = getCurrentSession().createQuery(
                    "SELECT j FROM Job j WHERE j.appUser.id = :userId", Job.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}
