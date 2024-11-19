package com.udise.portal.service.job.job_manager.impl;

import com.udise.portal.dao.JobDao;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.job.job_manager.JobManager;
import com.udise.portal.service.job.job_record_manager.JobRecordManager;
import com.udise.portal.service.udise_manager.UdiseManager;
import com.udise.portal.vo.job.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class JobManagerImpl implements JobManager {

    private static final Logger log = LogManager.getLogger(JobManagerImpl.class);
    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobRecordManager jobRecordManager;
    @Autowired
    private JobRecordDao jobRecordDao;

    @Autowired
    private DockerManager dockerManager;

    @Autowired
    @Qualifier("taskExecutor") // Specify the bean name to be used
    private TaskExecutor taskExecutor;

    @Autowired
    private UdiseManager udiseManager;

    @Override
    public List<JobResVo> getJobs(Long userId) {
        List<Job> jobList= jobDao.getJobs(userId);
        List<JobResVo> res=new ArrayList<>();
        for(Job job:jobList){
            JobResVo obj=new JobResVo();
            BeanUtils.copyProperties(job, obj);
            res.add(obj);
        }
        return res;
    }

    public JobStartResponseVo startJob(Long jobId) throws IOException, InterruptedException {
        Job job=jobDao.getById(Job.class,jobId);
        return udiseManager.startJob(jobId,job);
    }
}
