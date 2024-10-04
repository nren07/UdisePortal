package com.udise.portal.vo.file_upload;

import com.udise.portal.enums.ExecutionStatus;
import com.udise.portal.enums.FileType;
import com.udise.portal.enums.JobStatus;

import java.util.Date;

public class UploadFileResVo {

    private Long id=1L;

    private FileType fileType;

    private String fileUploadPath;

    private Date uploadedOn;

    private String fileName;

    private ExecutionStatus status;

    private JobStatus jobStatus;

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

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public UploadFileResVo(Long id, FileType fileType, String fileUploadPath, Date uploadedOn, String fileName, ExecutionStatus status) {
        this.id = id;
        this.fileType = fileType;
        this.fileUploadPath = fileUploadPath;
        this.uploadedOn = uploadedOn;
        this.fileName = fileName;
        this.status = status;
    }

    public UploadFileResVo() {
    }
}
