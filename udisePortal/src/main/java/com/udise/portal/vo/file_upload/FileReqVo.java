package com.udise.portal.vo.file_upload;

import com.udise.portal.enums.JobType;
import org.springframework.web.multipart.MultipartFile;

public class FileReqVo {

    private MultipartFile file;
    private JobType jobType;
    private String jobTitle;

    // Getters and setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}



