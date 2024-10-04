package com.udise.portal.service.file;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;


public interface FileManager {

    public File downloadFile(String key);

    public String uploadFile(MultipartFile uploadedFile, String fileFolder);

    // public String uploadFiles(InputStream file,String fileFolder);

    public void deleteFile(String key);

    public String updateFile(MultipartFile uploadedFile, String key);

//    List<FileUploadVo> listFiles(String pathPrefix);

    String uploadFileWithName(MultipartFile uploadedFile, String fileFolder);

//    List<FileUploadVo> getUploadedFilesList(FileUploadConfig fileUploadConfig, String pipelineName);

//    boolean deleteFile(FileUploadConfig fileUploadConfig, String fileName);

//    void moveFile(String origin, String destination);

    String getTempUrl(String key);

    String uploadFileWithName(File file, String fileName, String fileFolder);

    String getPreSignedURL(String fileUploadPath);
}
