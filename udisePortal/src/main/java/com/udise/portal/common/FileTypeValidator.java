package com.udise.portal.common;

import com.udise.portal.vo.file_upload.ValidFileType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<ValidFileType, String> {

    private static String[] exeTypes = { "pdf", "doc", "docx", "xls", "xlsx", "jpg", "jpeg", "png", "tif", "txt",
            "csv" };

    @Override
    public void initialize(ValidFileType arg0) {

    }

    @Override
    public boolean isValid(String fileName, ConstraintValidatorContext arg1) {
        if (StringUtils.isNotBlank(fileName)) {

            List<String> list = Arrays.asList(exeTypes);

            return list.contains(FilenameUtils.getExtension(fileName.toLowerCase()));
        }
        return true;

    }

}

