package com.udise.portal.controller;


import com.udise.portal.service.job_manager.JobManager;

import com.udise.portal.vo.job.JobResVo;
import com.udise.portal.vo.job.JobStartResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController("JobController")
@RequestMapping("/v1/job")
public class JobController {

    @Autowired
    private JobManager jobManager;


    @RequestMapping(value = "/{userId}/getJobs", method = RequestMethod.GET)
    public ResponseEntity<List<JobResVo>> getJobs(@PathVariable Long userId) {
        List<JobResVo> res=jobManager.getJobs(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/{jobId}/start", method = RequestMethod.GET)
    public ResponseEntity<JobStartResponseVo> startJob(@PathVariable Long jobId) throws IOException, InterruptedException {
        try{
            JobStartResponseVo res = jobManager.startJob(jobId);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
}
