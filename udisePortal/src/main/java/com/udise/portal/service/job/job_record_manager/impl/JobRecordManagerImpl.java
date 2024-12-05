package com.udise.portal.service.job.job_record_manager.impl;

import com.udise.portal.dao.JobDao;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.enums.JobType;
import com.udise.portal.service.job.job_record_manager.JobRecordManager;
import com.udise.portal.vo.job.JobRecordResponseVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class JobRecordManagerImpl implements JobRecordManager {

    private static final Logger log = LogManager.getLogger(JobRecordManagerImpl.class);
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private JobRecordDao jobRecordDao;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private Type1 type1;
    @Autowired
    private Type2 type2;
    @Autowired
    private Type3 type3;

    @Async
    public void createJobRecord(Job job, MultipartFile file) {
            if(job.getJobType()== JobType.SERVICE1){
                type1.createJobRecord(job,file);
            }else if(job.getJobType()== JobType.SERVICE2){
                type2.createJobRecord(job,file);
            }else{
                type3.createJobRecord(job,file);
            }
    }

    @Override
    public List<JobRecordResponseVo> getJobRecords(Long jobId) {
        List<JobRecord>records=jobRecordDao.getAllJobRecords(jobId);
        List<JobRecordResponseVo>responseVoList=new ArrayList<>();
        boolean isJobComplete=true;
        for(JobRecord record:records){
            JobRecordResponseVo obj=new JobRecordResponseVo();
            BeanUtils.copyProperties(record,obj);
            obj.setAttendence(record.getAttendance());
            obj.setPercentange(record.getPercentage());
            obj.setJobStatus(record.getJobStatus());
            if(obj.getJobStatus()!= JobStatus.COMPLETED){
                isJobComplete=false;
            }
            responseVoList.add(obj);
        }
        if(isJobComplete && !records.isEmpty()){
            Job job=records.get(0).getJob();
            jobDao.update(job);
        }
        return responseVoList;
    }

    public List<JobRecord>getJobRecord(Long jobId){
        return jobRecordDao.getAllJobRecords(jobId);
    }

    @Override
    public List<JobRecord> getListByQuery(String query,Long jobId,String className,String section) {
        return jobRecordDao.getListByQuery(query,jobId,className,section );
    }



}
