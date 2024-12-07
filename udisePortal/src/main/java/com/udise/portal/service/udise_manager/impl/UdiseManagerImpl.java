package com.udise.portal.service.udise_manager.impl;

import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobType;
import com.udise.portal.service.docker_manager.DockerManager;
import com.udise.portal.service.job.job_record_manager.JobRecordManager;
import com.udise.portal.service.udise_manager.UdiseManager;
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
    private UdiseService1 udiseService1;
    @Autowired
    private UdiseService2 udiseService2; //Add new students
    @Autowired
    private UdiseService3 udiseService3; // update profiles
    private Map<Long,Boolean> liveJobs;

    private static final Logger log = LogManager.getLogger(UdiseManagerImpl.class);

    public UdiseManagerImpl(){
        this.liveJobs=new HashMap<>();
    }

    @Override
    public JobStartResponseVo startJob(Long jobId, Job job) throws IOException, InterruptedException {
        if(liveJobs.containsKey(jobId)) return new JobStartResponseVo(null,"Job Already in Progress");
        List<JobRecord> jobRecordList=jobRecordManager.getJobRecord(jobId);
        if(jobRecordList.size()>0){
            liveJobs.put(jobId,true);
//            DockerVo dockerVo=dockerManager.createAndStartContainer(jobId);
            DockerVo dockerVo=new DockerVo();
            dockerVo.setVncPort(1000);
            dockerVo.setHostPort(2000);
            dockerVo.setContainerName("1234");
            dockerVo.setContainerId("1234");
//            if(dockerVo==null){
//                return new JobStartResponseVo(null,"internal server error");
//            }
//            String checkDockerStatus = String.format("http://%s:%d/", dockerVo.getContainerName(), 4444); //for prod
            String checkDockerStatus = String.format("http://localhost:%d/", dockerVo.getHostPort()); //for dev
//            dockerManager.waitForContainerReady(checkDockerStatus,dockerVo.getContainerId(),dockerVo);
            taskExecutor.execute(() -> {
                try {
                    if(job.getJobType()== JobType.SERVICE1){
                        udiseService1.startChromeService(dockerVo, dockerVo.getContainerId(), jobRecordList,job,liveJobs);
                    }else if(job.getJobType()== JobType.SERVICE2){
                        udiseService2.startChromeService(dockerVo, dockerVo.getContainerId(), jobRecordList,job,liveJobs);
                    }else{
                        udiseService3.startChromeService(dockerVo, dockerVo.getContainerId(), jobRecordList,job,liveJobs);
                    }
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return new JobStartResponseVo(dockerVo.getVncPort(),"Job Started");
        }else{
            return new JobStartResponseVo(null,"Record Not Found");
        }

    }
}
