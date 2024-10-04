package com.udise.portal.controller;


import com.udise.portal.entity.JobRecord;
import com.udise.portal.service.file.FileManager;
import com.udise.portal.service.job.JobRecordManager;

import com.udise.portal.vo.file_upload.UploadFileReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController("JobController")
@RequestMapping("/v1/job")
public class JobController {

    @Autowired
    private FileManager fileManager;

    @Autowired
    private JobRecordManager jobRecordManager;

    @RequestMapping(value = "/{userId}create", method = RequestMethod.POST)
    @ResponseBody
    public String save(@PathVariable Long userId, @RequestBody UploadFileReqVo uploadFile){
        File file = fileManager.downloadFile(uploadFile.getFileUploadPath());
        if (file != null && file.exists()) {
            List<JobRecord> records = jobRecordManager.save(userId,file);
        } else {
            System.out.println("File does not exist or could not be downloaded.");
        }
        return "Job Created Successfully";
    }
}
