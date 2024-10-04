package com.udise.portal.controller;

import com.udise.portal.service.upload.UploadFileManager;
import com.udise.portal.vo.file_upload.UploadFileReqVo;
import com.udise.portal.vo.file_upload.UploadFileResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("JobUploadController")
@RequestMapping("/upload")
public class UploadFileController  {

    @Autowired
    private UploadFileManager uploadFileManager;

    @RequestMapping(value = "/{userId}/job",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UploadFileResVo> save(@PathVariable Long userId,@RequestBody UploadFileReqVo uploadFile) {
        UploadFileResVo savedFile = uploadFileManager.save(userId,uploadFile);
        return new ResponseEntity<UploadFileResVo>(savedFile, HttpStatus.CREATED);
    }
}
