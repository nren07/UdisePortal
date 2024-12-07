package com.udise.portal.service.error_log.impl;

import com.udise.portal.dao.ErrorLogDao;
import com.udise.portal.entity.ErrorLog;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.service.error_log.ErrorLogManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class ErrorLogManagerImpl implements ErrorLogManager {
    @Autowired
    private ErrorLogDao errorLogDao;

    public void logError(JobRecord jobRecord, Throwable exception, String contextInfo, String severity, String sourceClass, String sourceMethod) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setJobRecord(jobRecord);
        errorLog.setTimestamp(LocalDateTime.now());
        errorLog.setErrorMessage(exception.getMessage());
        errorLog.setStackTrace(getStackTraceAsString(exception));
        errorLog.setContextInfo(contextInfo);
        errorLog.setSeverity(severity);
        errorLog.setSourceClass(sourceClass);
        errorLog.setSourceMethod(sourceMethod);
        errorLogDao.save(errorLog);
    }

    private String getStackTraceAsString(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
