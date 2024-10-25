package com.udise.portal.service.job;

import com.udise.portal.vo.job.JobRecordResponseVo;
import com.udise.portal.vo.job.JobResVo;
import com.udise.portal.vo.job.JobStartResponseVo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface JobManager {
    public List<JobResVo> getJobs(Long userId);
    public JobStartResponseVo startJob(Long jobId) throws IOException, InterruptedException;

}
