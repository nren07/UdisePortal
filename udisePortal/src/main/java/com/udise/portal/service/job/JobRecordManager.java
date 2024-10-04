package com.udise.portal.service.job;

import com.udise.portal.entity.JobRecord;

import java.io.File;
import java.util.List;

public interface JobRecordManager {
    public List<JobRecord> save(Long userId,File file);
}
