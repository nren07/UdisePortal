package com.udise.portal.dao;

import com.udise.portal.entity.FileValidation;
import com.udise.portal.enums.FileType;

import java.awt.color.ICC_Profile;

public interface FileValidationDao extends AbstractDao {

   // public abstract ListResponse<FileValidation> getGlobalFileValidation();

//    public abstract FileValidation getById(Long id);

   // public abstract ListResponse<FileValidation> getByReconId(Long reconId);

    void deleteGlobalFileValidations(FileType fileType);


}