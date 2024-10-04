package com.udise.portal.controller;

import com.udise.portal.service.file.FileManager;
import com.udise.portal.vo.file_upload.PathSavedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("FileUploadController")
@RequestMapping(value = "/v1/fileupload")
public class FileUploadController {

    @Autowired
    private FileManager fileManager;

    public static final String FILE_PATH_CIRCULAR_DOC = "udise";

    @PostMapping("/upload")
    public ResponseEntity<PathSavedVo> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath = fileManager.uploadFile(file, FILE_PATH_CIRCULAR_DOC);
        return new ResponseEntity<PathSavedVo>(new PathSavedVo(filePath, fileName), HttpStatus.OK);
    }

}