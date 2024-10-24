package com.udise.portal.vo.job;

import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.ClassStatus;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StandardVo {
    private ClassStatus classStatus;
    private String className;
    List<WebElement> sectionList;
    List<JobRecord>jobRecords;

    public StandardVo() {
    }

    public StandardVo(ClassStatus classStatus, String className, List<WebElement> sectionList, List<JobRecord> jobRecords) {
        this.classStatus = classStatus;
        this.className = className;
        this.sectionList = sectionList;
        this.jobRecords = jobRecords;
    }

    public ClassStatus getClassStatus() {
        return classStatus;
    }

    public void setClassStatus(ClassStatus classStatus) {
        this.classStatus = classStatus;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<WebElement> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<WebElement> sectionList) {
        this.sectionList = sectionList;
    }

    public List<JobRecord> getJobRecords() {
        return jobRecords;
    }

    public void setJobRecords(List<JobRecord> jobRecords) {
        this.jobRecords = jobRecords;
    }
}
