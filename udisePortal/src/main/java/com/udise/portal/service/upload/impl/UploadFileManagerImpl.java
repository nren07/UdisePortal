package com.udise.portal.service.upload.impl;
import com.udise.portal.dao.AbstractDao;
import com.udise.portal.dao.AppUserDao;
import com.udise.portal.dao.FileValidationDao;
import com.udise.portal.dao.UploadFileDao;
import com.udise.portal.entity.AppUser;
import com.udise.portal.entity.Job;
import com.udise.portal.enums.ExecutionStatus;
import com.udise.portal.enums.FileType;
import com.udise.portal.enums.JobStatus;
import com.udise.portal.enums.JobTitle;
import com.udise.portal.exception.ResourceNotFoundException;
import com.udise.portal.service.Abstract.impl.AbstractManagerImpl;
import com.udise.portal.service.file.FileManager;
import com.udise.portal.service.upload.UploadFileManager;
import com.udise.portal.vo.file_upload.UploadFileReqVo;
import com.udise.portal.vo.file_upload.UploadFileResVo;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

@Service
@Transactional
public class UploadFileManagerImpl extends AbstractManagerImpl implements UploadFileManager {

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private FileManager fileManager;


    public UploadFileManagerImpl(AbstractDao abstractDao) {
        super(abstractDao);
    }

    @Override
    @PreAuthorize("@authorizationManager.isAuthorized('uploadFileManager.save')")
    public UploadFileResVo save(Long id,UploadFileReqVo uploadFilePost) {
        AppUser user = appUserDao.getById(AppUser.class,id);
        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        Assert.notNull(uploadFilePost, "UploadFile cannot be null");
        Job job = new Job();
        BeanUtils.copyProperties(uploadFilePost, job);
        job.setUploadedOn(new Date());
        BeanUtils.copyProperties(uploadFilePost, job);
        job.setAppUser(user);
        job.setJobStatus(JobStatus.PENDING);
        if (job.getFileUploadPath() == null) {
            throw new ResourceNotFoundException("UploadFile.FILE_PATH_NOT_FOUND");
        }

        int index = job.getFileUploadPath().lastIndexOf(".");
        String extention = job.getFileUploadPath().substring(index += 1).toLowerCase();
        job.setExecutionStatus(ExecutionStatus.SUCCESS);
        job.setFileType(FileType.ITC);
        job.setJobTitle(JobTitle.UDISE);
        Job file = super.save(job);
        UploadFileResVo uploadFileResVo = new UploadFileResVo();
        BeanUtils.copyProperties(file, uploadFileResVo);
        return uploadFileResVo;
    }

    @Override
    public Job update(UploadFileReqVo uploadFile) {
        return null;
    }

    @Override
    public Job getById(Long id) {
        return null;
    }
}
