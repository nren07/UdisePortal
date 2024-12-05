package com.udise.portal.vo.job;

import com.udise.portal.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRecordResponseVo {
    private String studentName;
    private String className;
    private String section;
    private Long studentPen;
    private Long attendance;
    private Double percentage;
    private JobStatus jobStatus;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Long getStudentPen() {
        return studentPen;
    }

    public void setStudentPen(Long studentPen) {
        this.studentPen = studentPen;
    }

    public Long getAttendence() {
        return attendance;
    }

    public void setAttendence(Long attendence) {
        this.attendance = attendence;
    }

    public Double getPercentange() {
        return percentage;
    }

    public void setPercentange(Double percentange) {
        this.percentage = percentange;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
