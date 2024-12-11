package com.udise.portal.service.udise_manager.udise_manager.impl;

import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobType;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.job_record_manager.JobRecordManager;
import com.udise.portal.service.udise_manager.udise_services.ProgressionActivity;
import com.udise.portal.service.udise_manager.udise_services.UdiseAddStudent;
import com.udise.portal.service.udise_manager.udise_services.UdiseUpdateStudent;
import com.udise.portal.service.udise_manager.udise_manager.UdiseManager;
import com.udise.portal.vo.docker.DockerVo;
import com.udise.portal.vo.job.JobStartResponseVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UdiseManagerImpl implements UdiseManager {

    @Autowired
    private DockerManager dockerManager;

    @Autowired
    private JobRecordManager jobRecordManager;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private ProgressionActivity progressionActivity;
    @Autowired
    private UdiseUpdateStudent udiseUpdateStudent; //Add new students
    @Autowired
    private UdiseAddStudent udiseAddStudent; // update profiles
    private Map<Long,Boolean> liveJobs;

    private static final Logger log = LogManager.getLogger(UdiseManagerImpl.class);

    public UdiseManagerImpl(){
        this.liveJobs=new HashMap<>();
    }

    @Override
    public JobStartResponseVo startJob(Long jobId, Job job) throws IOException, InterruptedException,RuntimeException {
        log.info("startJob Function start");
        if(liveJobs.containsKey(jobId)) throw new RuntimeException("Job Already in progress");
        List<JobRecord> jobRecordList=jobRecordManager.getJobRecord(jobId);
        long remainingCredit=job.getAppUser().getClient().getCreditPoint();
        if(remainingCredit<jobRecordList.size()) throw new RuntimeException("Sufficient credit point not available");
        if(jobRecordList.size()>0){

            DockerVo dockerVo=dockerManager.createAndStartContainer(jobId);
            if(dockerVo==null){
                throw new RuntimeException("Internal Server Error");
            }
            String checkDockerStatus = String.format("http://%s:%d/", dockerVo.getContainerName(), 4444); //for prod
            log.info("wait for docker ready");
            dockerManager.waitForContainerReady(checkDockerStatus,dockerVo.getContainerId(),dockerVo); // for prod
            //            String checkDockerStatus = String.format("http://localhost:%d/", dockerVo.getHostPort()); //for dev
            taskExecutor.execute(() -> {
                try {
                    liveJobs.put(jobId,true);
                    if(job.getJobType()== JobType.PROGRESSION_ACTIVITY){
                        log.info("job start in progression");
                        progressionActivity.startChromeService(dockerVo, dockerVo.getContainerId(), jobRecordList,job,liveJobs);
                    }else if(job.getJobType()== JobType.UPDATE_STUDENTS){
                        log.info("job start in update student");
                        udiseUpdateStudent.startChromeService(dockerVo, dockerVo.getContainerId(), jobRecordList,job,liveJobs);
                    }else{
                        log.info("job start in add student");
                        udiseAddStudent.startChromeService(dockerVo, dockerVo.getContainerId(), jobRecordList,job,liveJobs);
                    }
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return new JobStartResponseVo(dockerVo.getVncPort(),"Job Started");
        }else{
            throw new RuntimeException("Record not found");
        }

    }
}
