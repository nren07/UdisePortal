package com.udise.portal.service.file.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.udise.portal.common.Assert;
import com.udise.portal.exception.CoreRuntimeException;
import com.udise.portal.service.file.FileManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class AWSFileManagerImpl implements FileManager, InitializingBean {

    private static final String TEMP = "temp";

    private static final String SLASH = "/";

    private static final String FILE_PATH = File.separator + "image";

    private static final String DOT = ".";

    private static final String HYPHEN = "-";

    private static final int RANDOM_STRING_LENGTH = 20;

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSFileManagerImpl.class);

    private static final String CONTENT_TYPE = "application/download";

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.credentials.access-key:#{null}}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:#{null}}")
    private String secretAccessKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired(required = true)
    private AmazonS3 s3client;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notBlank(bucketName, "'bucketName' is required.");
        Assert.notBlank(region, "'region' is required.");
    }

    @Override
    public File downloadFile(String key) {
        Assert.notBlank(key, "'key' is required.");
        File downloadedFile = null;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            LOGGER.info("Downloading s3://{}/{}", bucketName, key);
            S3Object object = s3client.getObject(getObjectRequest);
            downloadedFile = createDownloadFile(object);
        } catch (AmazonServiceException ase) {
            throw new CoreRuntimeException(null, ase.getErrorMessage(), ase);
        } catch (AmazonClientException ace) {
            throw new CoreRuntimeException(null, ace.getMessage(), ace);
        } catch (IOException ex) {
            throw new CoreRuntimeException(null, ex.getMessage(), ex);
        }
        return downloadedFile;
    }

    @Override
    public String uploadFile(MultipartFile fileToUpload, String fileFolder) {
        Assert.notNull(fileToUpload, "'fileToUpload' is required.");

        /*
         * try { File directory = new File(FILE_PATH); File fileToStore = new
         * File(directory + File.separator + RandomStringUtils.randomAlphanumeric(15) +
         * DOT + FilenameUtils.getExtension(fileToUpload.getOriginalFilename()));
         * FileUtils.writeByteArrayToFile(fileToStore, fileToUpload.getBytes()); return
         * fileToStore.getAbsolutePath(); } catch (IOException e) { e.printStackTrace();
         * return null; }
         */

        String objectKey = null;
        try {

            String randomKey = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
            objectKey = fileFolder + SLASH + randomKey + HYPHEN + fileToUpload.getOriginalFilename();
            s3client.putObject(new PutObjectRequest(bucketName, objectKey, createUploadFile(fileToUpload)));

        } catch (AmazonServiceException ase) {

            throw new CoreRuntimeException(null, ase.getErrorMessage(), ase);

        } catch (AmazonClientException ace) {

            throw new CoreRuntimeException(null, ace.getMessage(), ace);

        } catch (IOException ex) {

            throw new CoreRuntimeException(null, ex.getMessage(), ex);
        }

        return objectKey;

    }

    @Override
    public String updateFile(final MultipartFile fileToUpdate, final String key) {
        Assert.notNull(fileToUpdate, "'fileToUpdate' is required.");

        /*
         * try { File directory = new File(FILE_PATH); File fileToStore = new
         * File(directory + File.separator + RandomStringUtils.randomAlphanumeric(15) +
         * DOT + FilenameUtils.getExtension(fileToUpdate.getOriginalFilename()));
         *
         * FileUtils.writeByteArrayToFile(fileToStore, fileToUpdate.getBytes()); return
         * fileToStore.getAbsolutePath(); } catch (IOException e) { e.printStackTrace();
         * return null; }
         */

        Assert.notBlank(key, "'key' is required.");

        try {
            s3client.putObject(new PutObjectRequest(bucketName, key, createUploadFile(fileToUpdate)));
        } catch (AmazonServiceException ase) {

            throw new CoreRuntimeException(null, ase.getErrorMessage(), ase);

        } catch (AmazonClientException ace) {

            throw new CoreRuntimeException(null, ace.getMessage(), ace);

        } catch (IOException ex) {

            throw new CoreRuntimeException(null, ex.getMessage(), ex);

        }

        return key;

    }

    @Override
    public void deleteFile(String key) {
        Assert.notBlank(key, "'key' is required.");

        try {
            s3client.deleteObject(bucketName, key);
        } catch (AmazonServiceException ase) {
            throw new CoreRuntimeException(null, ase.getErrorMessage(), ase);
        } catch (AmazonClientException ace) {
            throw new CoreRuntimeException(null, ace.getMessage(), ace);
        }
    }

    private File createUploadFile(MultipartFile uploadedFile) throws IOException {
        File file = File.createTempFile(uploadedFile.getName(), uploadedFile.getOriginalFilename());
        file.deleteOnExit();
        FileUtils.writeByteArrayToFile(file, uploadedFile.getBytes());

        return file;
    }

    private File createDownloadFile(S3Object object) throws IOException {

        String key = object.getKey();
        String[] str = key.split(SLASH);
        int length = str.length;
        File file = File.createTempFile(TEMP, str[length - 1]);
        IOUtils.copy(object.getObjectContent(), new FileOutputStream(file));
        file.deleteOnExit();
        return file;
    }

//    @Override
//    public List<FileUploadVo> listFiles(String pathPrefix) {
//        List<FileUploadVo> fileUploadVoList = new ArrayList<>();
//        ListObjectsV2Result listObjectsV2Result;
//
//        LOGGER.info("S3Client: Request received to list files in bucket: {} for path: {}", bucketName, pathPrefix);
//        pathPrefix = pathPrefix.trim();
//        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//                .withPrefix(pathPrefix);
//
//        // By default it returns max 1000 records.
//        do {
//            listObjectsV2Result = s3client.listObjectsV2(listObjectsV2Request);
//            for (S3ObjectSummary s3ObjectSummary : listObjectsV2Result.getObjectSummaries()) {
//                // Remove empty entry if any for base path prefix.
//                if (s3ObjectSummary.getKey().equals(pathPrefix)
//                        || s3ObjectSummary.getKey().equals(pathPrefix.concat("/"))) {
//                    continue;
//                }
//                fileUploadVoList.add(new FileUploadVo(s3ObjectSummary.getKey(), s3ObjectSummary.getSize(),
//                        s3ObjectSummary.getLastModified()));
//            }
//            String nextCallToken = listObjectsV2Result.getNextContinuationToken();
//            listObjectsV2Request.setContinuationToken(nextCallToken);
//        } while (listObjectsV2Result.isTruncated());
//
//        return fileUploadVoList;
//    }

    @Override
    public String uploadFileWithName(MultipartFile uploadedFile, String fileFolder) {

        Assert.notNull(uploadedFile, "'fileToUpload' is required.");
        String fileName = null;
        try {
            fileName = fileFolder + SLASH + uploadedFile.getOriginalFilename();
            s3client.putObject(new PutObjectRequest(bucketName, fileName, createUploadFile(uploadedFile)));
        } catch (AmazonServiceException ase) {
            throw new CoreRuntimeException(null, ase.getErrorMessage(), ase);
        } catch (AmazonClientException ace) {
            throw new CoreRuntimeException(null, ace.getMessage(), ace);
        } catch (IOException ex) {
            throw new CoreRuntimeException(null, ex.getMessage(), ex);
        }
        return fileName;
    }

//    @Override
//    public List<FileUploadVo> getUploadedFilesList(FileUploadConfig fileUploadConfig, String pipelineName) {
//        String pathPrefix = fileUploadConfig.getPath();
//        String pipelineId = fileUploadConfig.getPipelineId();
//        FileSourceType fileSourceType = fileUploadConfig.getFileSourceType();
//        List<FileUploadVo> fileUploadVoList = new ArrayList<>();
//        ListObjectsV2Result listObjectsV2Result;
//
//        LOGGER.info("S3Client: Request received to list files in bucket: {} for path: {}", bucketName, pathPrefix);
//        pathPrefix = pathPrefix.trim();
//        if (!pathPrefix.endsWith(SLASH)) {
//            pathPrefix += SLASH;
//        }
//        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//                .withPrefix(pathPrefix);
//
//        // By default it returns max 1000 records.
//        do {
//            listObjectsV2Result = s3client.listObjectsV2(listObjectsV2Request);
//            for (S3ObjectSummary s3ObjectSummary : listObjectsV2Result.getObjectSummaries()) {
//                // Remove "folder" entries
//                if (s3ObjectSummary.getKey().endsWith("/")) {
//                    continue;
//                }
//                String filePath = s3ObjectSummary.getKey();
//                String filePathRelativeToPrefix = filePath.replaceFirst(pathPrefix, "");
//                fileUploadVoList.add(new FileUploadVo(filePathRelativeToPrefix, s3ObjectSummary.getSize(),
//                        s3ObjectSummary.getLastModified(), fileUploadConfig, pipelineName));
//            }
//            String nextCallToken = listObjectsV2Result.getNextContinuationToken();
//            listObjectsV2Request.setContinuationToken(nextCallToken);
//        } while (listObjectsV2Result.isTruncated());
//
//        return fileUploadVoList;
//    }

//    @Override
//    public boolean deleteFile(FileUploadConfig fileUploadConfig, String fileName) {
//        // Copy file from one directory to another
//        String origin = fileUploadConfig.getPath().concat(SLASH).concat(fileName);
//        String destination = fileUploadConfig.getDeleteFolderPath().concat(SLASH).concat(fileName);
//        moveFile(origin, destination);
//        return true;
//    }

//    @Override
//    public void moveFile(String origin, String destination) {
////		Copy file from one directory to another
//        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, origin, bucketName, destination);
//        s3client.copyObject(copyObjRequest);
//
////		Delete file from directory
//        DeleteObjectRequest deleteObjRequest = new DeleteObjectRequest(bucketName, origin);
//        s3client.deleteObject(deleteObjRequest);
//    }

    @Override
    public String getTempUrl(String key) {
        Date expTime = DateUtils.addMinutes(new Date(), 5);
        String tempUrl = s3client.generatePresignedUrl(bucketName, key, expTime, HttpMethod.GET).toString();
        LOGGER.info("Get Temp url : Got temp url for object {} from bucket {} till {}, url - {}", key, bucketName,
                expTime, tempUrl);
        return tempUrl;
    }

    @Override
    public String uploadFileWithName(File file, String fileName, String fileFolder) {
        Assert.notNull(file, "'fileToUpload' is required.");
        Assert.notNull(fileFolder, "'fileFolder' is required.");
        try {
            fileName = fileFolder + SLASH + fileName;
            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
        } catch (AmazonServiceException ase) {
            throw new CoreRuntimeException(null, ase.getErrorMessage(), ase);
        } catch (AmazonClientException ace) {
            throw new CoreRuntimeException(null, ace.getMessage(), ace);
        }
        return fileName;
    }

//    public ListObjectsV2Result getUploadedFilesObjectList(FileUploadConfig fileUploadConfig, String pipelineName) {
//        String pathPrefix = fileUploadConfig.getPath();
//        String pipelineId = fileUploadConfig.getPipelineId();
//        FileSourceType fileSourceType = fileUploadConfig.getFileSourceType();
////        List<FileUploadVo> fileUploadVoList = new ArrayList<>();
//        ListObjectsV2Result listObjectsV2Result;
//
//        LOGGER.info("S3Client: Request received to list files in bucket: {} for path: {}", bucketName, pathPrefix);
//        pathPrefix = pathPrefix.trim();
//        if (!pathPrefix.endsWith(SLASH)) {
//            pathPrefix += SLASH;
//        }
//        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//                .withPrefix(pathPrefix);
//
//        listObjectsV2Result = s3client.listObjectsV2(listObjectsV2Request);
//        return listObjectsV2Result;
//    }

    @Override
    public String getPreSignedURL(String Key) throws AmazonS3Exception {
        Instant currentUTCTime = Instant.now();
        Duration duration = Duration.ofMinutes(5);
        Instant expiredTime = currentUTCTime.plus(duration);
        ResponseHeaderOverrides responseHeaderOverrides = new ResponseHeaderOverrides();
        responseHeaderOverrides.setContentType(CONTENT_TYPE);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, Key)
                .withMethod(HttpMethod.GET).withResponseHeaders(responseHeaderOverrides)
                .withExpiration(Date.from(expiredTime));
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

}

