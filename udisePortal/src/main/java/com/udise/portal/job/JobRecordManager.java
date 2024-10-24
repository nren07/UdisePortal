package com.udise.portal.job;

import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.vo.job.JobRecordResponseVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface JobRecordManager {
    public void createTask(Job job, MultipartFile file);

    List<JobRecordResponseVo>getJobRecords(Long id);
    List<JobRecord>getJobRecord(Long id);
    List<JobRecord>getListByQuery(String query,Long jobId,String className, String section);
}
