package com.udise.portal.dao.impl;

import com.udise.portal.dao.FileValidationDao;
import com.udise.portal.entity.FileValidation;
import com.udise.portal.enums.FileType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FileValidationDaoImpl extends AbstractDaoImpl implements FileValidationDao {
//    @Override
//    public FileValidation getById(Long id) {
//        return null;
//    }

    @Override
    public void deleteGlobalFileValidations(FileType fileType) {

    }
}
