package com.udise.portal.entity;

import com.udise.portal.enums.FileSourceType;
import jakarta.persistence.*;

@Entity
@Table(name = "file_upload_config")public class FileUploadConfig extends AbstractEntity {

    private static final long serialVersionUID = -2008227642041410065L;

    public static final String DATA_SOURCE = "dataSource";

    public static final String PIPELINE_ID = "pipelineId";

    public static final String PIPELINE_SAMPLE_FILE_PATHS = "pipelineSampleFilePaths";

    private String fileName;

    private String path;

    private String deleteFolderPath;

    private String archivePath;

    private String pipelineId;

    private FileSourceType fileSourceType;

    private String validContentTypes;

    private String pipelineSampleFilePaths;

    public FileUploadConfig() {
        super();
    }

    public FileUploadConfig(Long id) {
        super(id);
    }

    @Column(name = "file_name", nullable = false, length = 200)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "path", nullable = false, length = 500)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "delete_file_path", nullable = false, length = 500)
    public String getDeleteFolderPath() {
        return deleteFolderPath;
    }

    public void setDeleteFolderPath(String deleteFolderPath) {
        this.deleteFolderPath = deleteFolderPath;
    }

    @Column(name = "archive_path", nullable = true, length = 500)
    public String getArchivePath() {
        return archivePath;
    }

    public void setArchivePath(String archivedPath) {
        this.archivePath = archivedPath;
    }

    @Column(name = "source_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public FileSourceType getFileSourceType() {
        return fileSourceType;
    }

    public void setFileSourceType(FileSourceType sourceType) {
        this.fileSourceType = sourceType;
    }

    @Column(name = "pipeline_id", nullable = false, length = 500)
    public String getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    @Column(name = "valid_content_types", nullable = true, length = 500)
    public String getValidContentTypes() {
        return validContentTypes;
    }

    public void setValidContentTypes(String validContentTypes) {
        this.validContentTypes = validContentTypes;
    }

    @Column(name = "sample_file_paths", nullable = false, length = 500)
    public String getPipelineSampleFilePaths() {
        return pipelineSampleFilePaths;
    }

    public void setPipelineSampleFilePaths(String pipelineSampleFile) {
        this.pipelineSampleFilePaths = pipelineSampleFilePaths;
    }
}
