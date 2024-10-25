package com.udise.portal.service.aws;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.udise.portal.vo.file_upload.FileReqVo;
import com.udise.portal.vo.job.JobResVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface AWSFileManager {
        public File downloadFile(String key);
//
    public String uploadFile(MultipartFile uploadedFile, String fileFolder);

//    public JobResVo fileHandler(FileReqVo req, Long userid);


//     public String uploadFiles(InputStream file, String fileFolder);

    public void deleteFile(String key);

    public String updateFile(MultipartFile uploadedFile, String key);

//    List<FileUploadVo> listFiles(String pathPrefix);

    String uploadFileWithName(MultipartFile uploadedFile, String fileFolder);

    String getTempUrl(String key);

    String uploadFileWithName(File file, String fileName, String fileFolder);

    String getPreSignedURL(String Key) throws AmazonS3Exception;

//    List<FileUploadVo> getUploadedFilesList(FileUploadConfig fileUploadConfig, String pipelineName);

//    boolean deleteFile(FileUploadConfig fileUploadConfig, String fileName);

//    void moveFile(String origin, String destination);

//    String getTempUrl(String key);

//    String uploadFileWithName(File file, String fileName, String fileFolder);

//    String getPreSignedURL(String fileUploadPath);
}
