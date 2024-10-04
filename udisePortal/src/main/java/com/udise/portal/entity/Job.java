package com.udise.portal.entity;

import com.udise.portal.enums.ExecutionStatus;
import com.udise.portal.enums.FileType;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.enums.JobTitle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="job_table")
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private FileType fileType;

    private String fileUploadPath;

    private Date uploadedOn;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;

    @Enumerated(EnumType.STRING)
    private ExecutionStatus executionStatus;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = AppUser.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private AppUser appUser;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = true, referencedColumnName = "id")
    private List<JobRecord> jobRecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileUploadPath() {
        return fileUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public List<JobRecord> getJobRecords() {
        return jobRecords;
    }

    public void setJobRecords(List<JobRecord> jobRecords) {
        this.jobRecords = jobRecords;
    }
}
