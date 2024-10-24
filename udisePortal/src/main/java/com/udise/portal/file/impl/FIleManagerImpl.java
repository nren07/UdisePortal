package com.udise.portal.file.impl;

import com.udise.portal.dao.AppUserDao;
import com.udise.portal.dao.FileDao;
import com.udise.portal.entity.AppUser;
import com.udise.portal.entity.Job;
import com.udise.portal.enums.ExecutionStatus;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.aws.impl.AWSFileManagerImpl;
import com.udise.portal.file.FileManager;
import com.udise.portal.job.JobRecordManager;
import com.udise.portal.vo.file_upload.FileReqVo;
import com.udise.portal.vo.job.JobResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class FIleManagerImpl extends AWSFileManagerImpl implements FileManager {

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private JobRecordManager jobRecordManager;

    @Autowired
    @Qualifier("taskExecutor") // Specify the bean name to be used
    private TaskExecutor taskExecutor;

    @Override
    public JobResVo fileHandler(FileReqVo req, Long userId) {
        AppUser user = appUserDao.getById(AppUser.class,userId);
        if(user==null) {
            throw new UsernameNotFoundException("User not found");
        }
        MultipartFile file=req.getFile();
        String fileName = file.getOriginalFilename();
//        String filePath=uploadFile(file,FILE_PATH_CIRCULAR_DOC);
        Job job=new Job();
        job.setAppUser(user);
        job.setJobTitle(req.getJobTitle());
        job.setExecutionStatus(ExecutionStatus.FILE_UPLOADED);
        job.setJobStatus(JobStatus.PENDING);
        job.setFileName(fileName);
//        job.setFileUploadPath(filePath);
        job.setUploadedOn(new Date());
        job.setJobType(req.getJobType());
        Job record = fileDao.save(job);
        job.setId(record.getId());
        JobResVo res=new JobResVo();
        res.setId(record.getId());
        res.setFileName(fileName);
        res.setExecutionStatus(ExecutionStatus.FILE_UPLOADED);
        res.setUploadedOn(job.getUploadedOn());
        // function to create job records from this job
//        File downloadedFile=downloadFile(filePath);
        taskExecutor.execute(()->jobRecordManager.createTask(record,file));
        return res;
    }
}
