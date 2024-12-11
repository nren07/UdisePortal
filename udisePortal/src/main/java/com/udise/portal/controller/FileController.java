package com.udise.portal.controller;

import com.udise.portal.service.file.FileManager;
import com.udise.portal.vo.file_upload.*;
import com.udise.portal.vo.job.JobResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("FileUploadController")
@RequestMapping(value = "/v1/fileupload")
public class FileController {
    public static final String FILE_PATH_CIRCULAR_DOC = "udise";

    @Autowired
    private FileManager fileManager;

    @PostMapping("/{userId}/upload")
    public ResponseEntity<JobResVo> uploadFile(
            @PathVariable Long userId,
            @ModelAttribute FileReqVo requestVo) throws Exception {


        JobResVo response = fileManager.fileHandler(requestVo, userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}