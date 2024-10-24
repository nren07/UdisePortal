package com.udise.portal.dao;

import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;

import java.util.List;


public interface JobRecordDao extends AbstractDao {
    List<JobRecord> getAllJobRecords(Long userId);
    List<JobRecord> getListByQuery(String query,Long jobId,String className,String section);
}
