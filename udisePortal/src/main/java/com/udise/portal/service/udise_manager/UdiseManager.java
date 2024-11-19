package com.udise.portal.service.udise_manager;

import com.udise.portal.entity.Job;
import com.udise.portal.vo.job.JobStartResponseVo;
import java.io.IOException;

public interface UdiseManager {
    public JobStartResponseVo startJob(Long jobId, Job job) throws IOException, InterruptedException;
}
