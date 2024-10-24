package com.udise.portal.dao;

import com.udise.portal.entity.Job;

import java.util.List;


public interface JobDao extends AbstractDao {
    public List<Job> getJobs(Long userId);
}
