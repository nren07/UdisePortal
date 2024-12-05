package com.udise.portal.common;

import com.udise.portal.exception.ResourceNotFoundException;
import com.udise.portal.exception.SystemFailureException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CoreUtil {
    private static final String DOT = ".";

    private static final int MAX_FILE_SIZE = 5242880;

    public static final String FILE_ROOT_PATH = File.separator + "documents";

    public static final String UNDERSCORE = "_";

    public static final String SLASH = "/";

    public static final String DATE_FORMATE_WITH_TIME = "ddMyyyyhhmmss";

    public static final String DATE_FORMATE = "ddMyyyy";

    public static boolean isNonZero(Long id) {
        boolean nonZero = (id != null && id > 0);
        return nonZero;
    }

    public static boolean isZero(Long id) {
        boolean zero = !isNonZero(id);
        return zero;
    }

    public static boolean isZero(Integer id) {
        boolean zero = !isNonZero(id);
        return zero;
    }

    public static boolean isNonZero(BigInteger id) {
        boolean nonZero = (id != null && id.intValue() > 0);
        return nonZero;
    }

    public static boolean isNonZero(Double id) {
        boolean nonZero = (id != null && id.intValue() > 0);
        return nonZero;
    }

    public static boolean isNonZero(Number id) {
        return (id != null && id.intValue() > 0);
    }

    public static boolean isNonZero(Integer id) {
        return (id != null && id > 0);
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isIdNotEqual(final Long id, final Long id2) {
        if (id != id2) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return CollectionUtils.isNotEmpty(coll);
    }

    public static boolean isEmpty(Collection<?> coll) {
        return CollectionUtils.isEmpty(coll);
    }

    public static boolean isNotBlank(String content) {
        return StringUtils.isNotBlank(content);
    }

    public static boolean isBlank(String content) {
        return StringUtils.isBlank(content);
    }

//    public static Object getClaim(final String claimName, final String jsonWebToken) {
//        SignedJWT signedJWT = null;
//        try {
//            signedJWT = SignedJWT.parse(jsonWebToken);
//        } catch (ParseException e) {
//            throw new BadCredentialsException("Bad credentials", e);
//        }
//        Object claim = null;
//        try {
//            ReadOnlyJWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
//            claim = claimsSet.getCustomClaim(claimName);
//        } catch (ParseException e) {
//            throw new BadCredentialsException("Bad credentials");
//        }
//
//        return claim;
//    }

//    public static String saveDocument(String filePath, String encodedFile, String fileName) {
//        byte[] decodedBytes = Base64.decode(encodedFile.getBytes());
//
//        if (decodedBytes.length > MAX_FILE_SIZE) {
//
//            throw new ResourceNotFoundException(CommonError.FILE_SIZE_INVALID);
//        }
//
//        File directory = new File(filePath);
//
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String extension = getExtension(fileName);
//
//        File fileToStore = new File(directory + File.separator + genrateRandomString(15) + DOT + extension);
//        try {
//            FileUtils.writeByteArrayToFile(fileToStore, decodedBytes);
//        } catch (IOException e) {
//
//            new SystemFailureException("Error while storing document", e);
//        }
//        return fileToStore.getAbsolutePath();
//    }

    public static String swapExtension(String currentFileName, String newFileName) {
        if (CoreUtil.isNull(currentFileName))
            return null;
        if (CoreUtil.isNull(newFileName))
            return currentFileName;

        String baseName = FilenameUtils.getBaseName(newFileName);
        String extension = FilenameUtils.getExtension(currentFileName);
        String fileName = baseName + DOT + extension;
        return fileName;

    }

    public static String genrateRandomString(int lengthOfNumber) {
        SecureRandom random = new SecureRandom();

        String source = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(lengthOfNumber);
        for (int i = 0; i < lengthOfNumber; i++)
            sb.append(source.charAt(random.nextInt(source.length())));
        return sb.toString();

    }

    public static String getExtension(String fileName) {
        String[] allowedExtensions = new String[] { "jpg", "jpeg", "gif", "png", "xls", "pdf", "csv", "xlsx" };

        String defaultExtension = "png";

        String extensionByUser = FilenameUtils.getExtension(fileName);

        for (String allowedExtension : allowedExtensions) {
            if (allowedExtension.equals(extensionByUser)) {
                defaultExtension = allowedExtension;
            }
        }
        return defaultExtension;
    }

//    public static MultipartFile getFile(String encodedFile, String fileName) {
//        byte[] decodedBytes = Base64.decode(encodedFile.getBytes());
//
//        if (decodedBytes.length > MAX_FILE_SIZE) {
//
//            throw new ResourceNotFoundException(CommonError.FILE_SIZE_INVALID);
//        }
//        return new MockMultipartFile(fileName, decodedBytes);
//    }

//    public static String getFilePath(String subPath) {
//        String directoryPath = subPath + SLASH + getDirectoryAppender();
//        return directoryPath;
//    }

//    public static String getFileName(String fileName, String fileExtension) {
//        return fileName.replaceAll("[^a-zA-Z0-9]", "") + UNDERSCORE + getFileNameAppender() + fileExtension;
//    }

//    public static String getFileNameAppender() {
//        return DatePattern.formatCurrentDate(DATE_FORMATE_WITH_TIME);
//    }

//    public static String getDirectoryAppender() {
//        return DatePattern.formatCurrentDate(DATE_FORMATE);
//    }

    public static File createTemplFile(String fileName, String fileExtension) throws IOException {
        File tempFile = File.createTempFile(fileName, fileExtension);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static List<String> convertDelimitedStringToList(String str, String delimeiter) {
        List<String> strList = null;
        if (CoreUtil.isNotNull(str)) {
            strList = Arrays.asList(str.split(delimeiter));
        }
        return strList;
    }

    public static Long convertIntToLongWithNullCheck(Integer intVal) {
        return isNull(intVal) ? null : Long.valueOf(intVal);
    }

    public static Date convertToDateWithNullCheck(Integer timestampVal) {
        return isNull(timestampVal) ? null : new Date(timestampVal * 1000L);
    }

    public static Long convertStringToLong(String strValue) {
        return StringUtils.isNotEmpty(strValue) && StringUtils.isNumeric(strValue) ? Long.valueOf(strValue) : null;
    }
}
