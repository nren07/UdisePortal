package com.udise.portal.service.error_log;

import com.udise.portal.entity.ErrorLog;
import com.udise.portal.entity.JobRecord;

public interface ErrorLogManager {
    public void logError(JobRecord jobRecord, Exception exception, String contextInfo, String severity, String sourceClass, String sourceMethod);
}
