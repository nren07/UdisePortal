package com.udise.portal.service.job.impl;

import com.udise.portal.dao.JobDao;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.enums.JobType;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.job.JobManager;
import com.udise.portal.service.job.JobRecordManager;
import com.udise.portal.vo.docker.DockerVo;
import com.udise.portal.vo.job.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private Udise1Service udise1Service;

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
        if(job.getJobType()== JobType.UDISE){
            log.info("JobType Udise Started");
            return udise1Service.startJob(jobId,job);
        }else if(job.getJobType()== JobType.DUMMY){
            log.info("JobType Dummy Started");
            return udise1Service.startJob(jobId,job);
        }else{
            log.info("JobType Other Started");
            return udise1Service.startJob(jobId,job);
        }
    }
}
