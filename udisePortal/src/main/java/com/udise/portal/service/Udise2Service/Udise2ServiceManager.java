package com.udise.portal.service.Udise2Service;

import com.udise.portal.entity.Job;
import com.udise.portal.vo.job.JobStartResponseVo;

import java.io.IOException;

public interface Udise2ServiceManager {
    public JobStartResponseVo startJob(Long jobId, Job job) throws IOException, InterruptedException;
}
