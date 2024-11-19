package com.udise.portal.service.job.job_record_manager;

import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.vo.job.JobRecordResponseVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobRecordManager {
    public void createJobRecord(Job job, MultipartFile file);
    List<JobRecordResponseVo>getJobRecords(Long id);
    List<JobRecord>getJobRecord(Long id);
    List<JobRecord>getListByQuery(String query,Long jobId,String className, String section);
}
