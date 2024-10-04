package com.udise.portal.common;

import com.udise.portal.dao.AbstractDao;

public interface FileValidationUtil extends AbstractDao {

    String validateFilePath(String path);

    boolean validateFilePathUsingRegExp(String path);

}
