package com.udise.portal.service.udise1service;

import com.udise.portal.entity.Job;
import com.udise.portal.vo.job.JobStartResponseVo;
import java.io.IOException;

public interface Udise1ServiceManager {
    public JobStartResponseVo startJob(Long jobId, Job job) throws IOException, InterruptedException;
}
