package com.udise.portal.entity;

import com.udise.portal.enums.Category;
import com.udise.portal.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="job_record_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class JobRecord2{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Job.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private String studentName;
    private String className;
    private String section;
    private Double studentPen;
    private Double attendance;
    private Double percentage;

    private String gender;
    private Double dob;
    private Double stateCode;
    private String motherName;
    private String fatherName;
    private String aadharNumber;
    private Double dateOfAdmission;
    private String address;
    private Double pinCode;
    private Double fatherMoNumber;
    private String motherTongue;
    private Category category;
    private String minorityGroup;
    private boolean bpl;
    private boolean ews;
    private boolean cwsn;
    private boolean oosc;     //Is this Student identified as Out-of-School-Child in current or previous years?
    private String bloodGroup;
    private Double admissionNumber;
    private String statusOfStudentPrevAcademic;  //4.2.5(a) Status of student in Previous Academic Year of Schooling
    private String enrolledUnder;
    private  boolean isGifted;
    private Double height;   //in cms
    private Double weight; //in KGs

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

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

    public Double getStudentPen() {
        return studentPen;
    }

    public void setStudentPen(Double studentPen) {
        this.studentPen = studentPen;
    }

    public Double getAttendance() {
        return attendance;
    }

    public void setAttendance(Double attendance) {
        this.attendance = attendance;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getDob() {
        return dob;
    }

    public void setDob(Double dob) {
        this.dob = dob;
    }

    public Double getStateCode() {
        return stateCode;
    }

    public void setStateCode(Double stateCode) {
        this.stateCode = stateCode;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Double getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(Double dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPinCode() {
        return pinCode;
    }

    public void setPinCode(Double pinCode) {
        this.pinCode = pinCode;
    }

    public Double getFatherMoNumber() {
        return fatherMoNumber;
    }

    public void setFatherMoNumber(Double fatherMoNumber) {
        this.fatherMoNumber = fatherMoNumber;
    }

    public String getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMinorityGroup() {
        return minorityGroup;
    }

    public void setMinorityGroup(String minorityGroup) {
        this.minorityGroup = minorityGroup;
    }

    public boolean isBpl() {
        return bpl;
    }

    public void setBpl(boolean bpl) {
        this.bpl = bpl;
    }

    public boolean isEws() {
        return ews;
    }

    public void setEws(boolean ews) {
        this.ews = ews;
    }

    public boolean isCwsn() {
        return cwsn;
    }

    public void setCwsn(boolean cwsn) {
        this.cwsn = cwsn;
    }

    public boolean isOosc() {
        return oosc;
    }

    public void setOosc(boolean oosc) {
        this.oosc = oosc;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Double getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(Double admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public String getStatusOfStudentPrevAcademic() {
        return statusOfStudentPrevAcademic;
    }

    public void setStatusOfStudentPrevAcademic(String statusOfStudentPrevAcademic) {
        this.statusOfStudentPrevAcademic = statusOfStudentPrevAcademic;
    }

    public String getEnrolledUnder() {
        return enrolledUnder;
    }

    public void setEnrolledUnder(String enrolledUnder) {
        this.enrolledUnder = enrolledUnder;
    }

    public boolean isGifted() {
        return isGifted;
    }

    public void setGifted(boolean gifted) {
        isGifted = gifted;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}


