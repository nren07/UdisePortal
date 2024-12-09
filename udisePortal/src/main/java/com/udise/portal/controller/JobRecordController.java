package com.udise.portal.controller;

import com.udise.portal.service.job_record_manager.JobRecordManager;
import com.udise.portal.vo.job.JobRecordResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("JobRecordController")
@RequestMapping("/v1/job_record")
public class JobRecordController {
    @Autowired
    private JobRecordManager jobRecordManager;


    @RequestMapping(value = "/{jobId}/get_job_records", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<JobRecordResponseVo>> getJobRecords(@PathVariable Long jobId) {
        List<JobRecordResponseVo> res=jobRecordManager.getJobRecords(jobId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
