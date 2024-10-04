package com.udise.portal.service.common_impl;

import com.udise.portal.common.FileValidationUtil;
import com.udise.portal.dao.impl.AbstractDaoImpl;
import com.udise.portal.entity.AppUser;
import com.udise.portal.exception.CoreRuntimeException;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@Service
@Transactional
public class FileValidationUtilImpl extends AbstractDaoImpl implements FileValidationUtil {

//	@Value("${portal.rootFolder}")
//	private String rootFolder;

    @Override
    @FilePathCleanser
    public String validateFilePath(String path) {

        // validate path using regExp
        validateFilePathUsingRegExp(path);

        // validate path using Conical Path
        String absolutePath = validatePathUsingConicalPath(path);

        // validate Extension
        absolutePath = validateExtension(absolutePath);

        // user validation

        // TODO Temporory deleted
        /*
         * AppUser loggedInUser = getLoggedInUser();
         *
         * if (loggedInUser == null) { throw new
         * UnauthorizedAccessException(ErrorCode.AppUserLoginErrors. NOT_AUTHORIZED); }
         *
         * Role loggedInUserRole = getLoggedInUserRole();
         *
         * if (loggedInUserRole == null) { throw new
         * UnauthorizedAccessException(ErrorCode.Role.SET_VALID_ROLE); }
         *
         * if (!userMayAccessFile(loggedInUser)) {
         *
         * throw new UnauthorizedAccessException(ErrorCode.FileValidationUtilErrors.
         * USER_FILE_ACCESS_ERROR); }
         */

        return absolutePath;
    }

    private String validatePathUsingConicalPath(String path) {
        File file = new File(path);
        String absolutePath = null;
        try {
            absolutePath = file.getCanonicalPath();

            // TODO: Temparory Deleted
            /*
             * if (!absolutePath.startsWith(rootFolder)) { throw new
             * InvalidArgumentException(ErrorCode.FileValidationUtilErrors.
             * FILE_PATH_ERROR); }
             */

            // TODO: Temparory Deleted
            /*
             * if (!file.exists()) { throw new
             * InvalidArgumentException(ErrorCode.FileValidationUtilErrors.
             * USER_FILE_ACCESS_ERROR); }
             */

        } catch (IOException e) {
            throw new CoreRuntimeException("Error while access a file", e);
        }
        return absolutePath;
    }

    private boolean userMayAccessFile(AppUser loggedInUser) {

        return loggedInUser == null ? false : true;
    }

    @Override
    @FilePathCleanser
    public boolean validateFilePathUsingRegExp(String path) {

        boolean valid = true;
        if (StringUtils.isNotBlank(path)) {

            Path pathObj = Paths.get(path);

            String fileName = pathObj.getFileName().toString();

            int index = fileName.lastIndexOf('.');
            String fileNameWithoutExe = fileName.substring(0, index);

            final Pattern pattern = Pattern.compile("^[0-9a-zA-Z]*$");
            // TODO: Temporory Commented
            /*
             * if (!pattern.matcher(fileNameWithoutExe).matches()) { throw new
             * InvalidArgumentException(ErrorCode.FileValidationUtilErrors.
             * INVALID_FILE_NAME_ERROR); }
             */


        } else {
            throw new InvalidArgumentException("invalid path");
        }

        return valid;
    }

    public static String validateExtension(String filePath) {
        String defaultExtension = "png";

        Path pathObj = Paths.get(filePath);

        String fileName = pathObj.getFileName().toString();

        if (StringUtils.isEmpty(fileName)) {
            return filePath;
        }

        String[] allowedExtensions = new String[] { "jpg", "jpeg", "gif", "png", "xls", "pdf", "csv", "xlsx", "zip" };

        String extensionByUser = FilenameUtils.getExtension(fileName);

        for (String allowedExtension : allowedExtensions) {
            if (allowedExtension.equals(extensionByUser)) {
                defaultExtension = allowedExtension;
            }
        }

        int index = fileName.lastIndexOf('.');
        String fileNameWithoutExe = fileName.substring(0, index);

        pathObj = pathObj.resolveSibling(fileNameWithoutExe + "." + defaultExtension);

        return pathObj.toAbsolutePath().toString();
    }

}

