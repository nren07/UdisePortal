package com.udise.portal.vo.file_upload;

public class PathSavedVo {

    private String path;

    private String fileName;

    public PathSavedVo() {
    }

    public PathSavedVo(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

