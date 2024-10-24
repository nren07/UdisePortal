package com.udise.portal.dao.impl;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobRecordDaoImpl extends AbstractDaoImpl implements JobRecordDao {
    private static final Logger log = LoggerFactory.getLogger(JobRecordDaoImpl.class);
    @Override
    public List<JobRecord> getAllJobRecords(Long id) {
        try {
            TypedQuery<JobRecord> query = getEm().createQuery(
                    "SELECT r FROM JobRecord r WHERE r.job.id = :id", JobRecord.class);
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public List<JobRecord> getListByQuery(String queryString, Long id, String className, String section) {
        try {
            String query2 = "SELECT r FROM JobRecord r " +
                    "WHERE r.job.id = :id " +
                    "AND LOWER(r.className) = LOWER(:className) " +
                    "AND LOWER(r.section) = LOWER(:section) " +
                    "AND r.jobStatus = :jobStatus";

            TypedQuery<JobRecord> query = getEm().createQuery(query2, JobRecord.class);

            // Logging parameters for better debugging
            log.info("id: {}, className: {}, section: {}, jobStatus: {}", id, className, section, JobStatus.PENDING);

            // Set parameters correctly
            query.setParameter("id", id);
            query.setParameter("className", className.trim());
            query.setParameter("section", section.trim());
            query.setParameter("jobStatus", JobStatus.PENDING);  // Pass enum instance

            return query.getResultList();
        } catch (Exception e) {
            log.error("Error executing query: {}", e.getMessage(), e);
            return null;
        }
    }

}
