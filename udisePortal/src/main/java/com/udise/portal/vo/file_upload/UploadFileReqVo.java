package com.udise.portal.vo.file_upload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UploadFileReqVo {

    @NotBlank(message = "{uploadFilePostVo.fileName.notBlank}")
    @Size(max = 255 ,message = "{uploadFilePostVo.fileName.size}")
    private String jobTitle;

    @NotBlank(message = "{uploadFilePostVo.fileName.notBlank}")
    @Size(max = 255 ,message = "{uploadFilePostVo.fileName.size}")
    private String jobType;

    @NotBlank(message = "{uploadFilePostVo.fileUploadPath.notBlank}")
    @Size(max = 500 ,message = "{uploadFilePostVo.fileUploadPath.size}")
    private String fileUploadPath;

    @NotBlank(message = "{uploadFilePostVo.fileName.notBlank}")
    @Size(max = 255 ,message = "{uploadFilePostVo.fileName.size}")
    private String fileName;

    public @NotBlank(message = "{uploadFilePostVo.fileName.notBlank}") @Size(max = 255, message = "{uploadFilePostVo.fileName.size}") String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(@NotBlank(message = "{uploadFilePostVo.fileName.notBlank}") @Size(max = 255, message = "{uploadFilePostVo.fileName.size}") String jobTitle) {
        this.jobTitle = jobTitle;
    }


    public @NotBlank(message = "{uploadFilePostVo.fileUploadPath.notBlank}") @Size(max = 500, message = "{uploadFilePostVo.fileUploadPath.size}") String getFileUploadPath() {
        return fileUploadPath;
    }

    public void setFileUploadPath(@NotBlank(message = "{uploadFilePostVo.fileUploadPath.notBlank}") @Size(max = 500, message = "{uploadFilePostVo.fileUploadPath.size}") String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public @NotBlank(message = "{uploadFilePostVo.fileName.notBlank}") @Size(max = 255, message = "{uploadFilePostVo.fileName.size}") String getFileName() {
        return fileName;
    }

    public void setFileName(@NotBlank(message = "{uploadFilePostVo.fileName.notBlank}") @Size(max = 255, message = "{uploadFilePostVo.fileName.size}") String fileName) {
        this.fileName = fileName;
    }

    public @NotBlank(message = "{uploadFilePostVo.fileName.notBlank}") @Size(max = 255, message = "{uploadFilePostVo.fileName.size}") String getJobType() {
        return jobType;
    }

    public void setJobType(@NotBlank(message = "{uploadFilePostVo.fileName.notBlank}") @Size(max = 255, message = "{uploadFilePostVo.fileName.size}") String jobType) {
        this.jobType = jobType;
    }
}