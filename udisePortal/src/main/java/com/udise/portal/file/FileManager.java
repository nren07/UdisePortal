package com.udise.portal.file;
import com.udise.portal.aws.AWSFileManager;
import com.udise.portal.vo.file_upload.FileReqVo;
import com.udise.portal.vo.job.JobResVo;
import org.springframework.web.multipart.MultipartFile;


public interface FileManager extends AWSFileManager {

    public JobResVo fileHandler(FileReqVo req, Long userid);

    String uploadFileWithName(MultipartFile uploadedFile, String fileFolder);

}
