package com.udise.portal.entity;

import com.udise.portal.enums.Category;
import com.udise.portal.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name="job_record_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class JobRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Job.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private String studentName;
    private String nameAsAadhar;
    private String className;
    private String section;
    private Long studentPen;
    private Long attendance;
    private Double percentage;

    private String gender;
    private Date dob;
    private Long stateCode;
    private String motherName;
    private String fatherName;
    private String aadharNumber;
    private Date dateOfAdmission;
    private String address;
    private Long pinCode;
    private Long fatherMoNumber;
    private String motherTongue;
    private Category category;
    private String minorityGroup;
    private boolean bpl;
    private boolean ews;
    private boolean cwsn;
    private boolean oosc;     //Is this Student identified as Out-of-School-Child in current or previous years?
    private boolean isSLD; //Specific Learning Disability (SLD)?
    private boolean isASD; //Autism Spectrum Disorder
    private boolean isADHD; //Attention Deficit Hyperactive Disorder
    private boolean isSportsChamp;
    private  boolean isGifted;
    private boolean isParticipatedNCC;
    private boolean isDigitalyLiterate;

    private String bloodGroup;
    private Long admissionNumber;
    private String statusOfStudentPrevAcademic;  //4.2.5(a) Status of student in Previous Academic Year of Schooling
    private String classStudiedInPreviousAcademicYear;
    private String resultOfExamination;
    private String enrolledUnder;

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

    public Long getStudentPen() {
        return studentPen;
    }

    public void setStudentPen(Double studentPen) {
        this.studentPen = studentPen.longValue();
    }

    public Long getAttendance() {
        return attendance;
    }

    public void setAttendance(Double attendance) {
        this.attendance = attendance.longValue();
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Double stateCode) {
        this.stateCode = stateCode.longValue();
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

    public Date getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(Date dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPinCode() {
        return pinCode;
    }

    public void setPinCode(Double pinCode) {
        this.pinCode = pinCode.longValue();
    }

    public Long getFatherMoNumber() {
        return fatherMoNumber;
    }

    public void setFatherMoNumber(Double fatherMoNumber) {
        this.fatherMoNumber = fatherMoNumber.longValue();
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

    public boolean isSLD() {
        return isSLD;
    }

    public void setSLD(boolean SLD) {
        isSLD = SLD;
    }

    public boolean isASD() {
        return isASD;
    }

    public void setASD(boolean ASD) {
        isASD = ASD;
    }

    public boolean isADHD() {
        return isADHD;
    }

    public void setADHD(boolean ADHD) {
        isADHD = ADHD;
    }

    public boolean isSportsChamp() {
        return isSportsChamp;
    }

    public void setSportsChamp(boolean sportsChamp) {
        isSportsChamp = sportsChamp;
    }

    public boolean isGifted() {
        return isGifted;
    }

    public void setGifted(boolean gifted) {
        isGifted = gifted;
    }

    public boolean isParticipatedNCC() {
        return isParticipatedNCC;
    }

    public void setParticipatedNCC(boolean participatedNCC) {
        isParticipatedNCC = participatedNCC;
    }

    public boolean isDigitalyLiterate() {
        return isDigitalyLiterate;
    }

    public void setDigitalyLiterate(boolean digitalyLiterate) {
        isDigitalyLiterate = digitalyLiterate;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Long getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(Double admissionNumber) {
        this.admissionNumber = admissionNumber.longValue();
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

    public String getClassStudiedInPreviousAcademicYear() {
        return classStudiedInPreviousAcademicYear;
    }

    public void setClassStudiedInPreviousAcademicYear(String classStudiedInPreviousAcademicYear) {
        this.classStudiedInPreviousAcademicYear = classStudiedInPreviousAcademicYear;
    }

    public String getResultOfExamination() {
        return resultOfExamination;
    }

    public void setResultOfExamination(String resultOfExamination) {
        this.resultOfExamination = resultOfExamination;
    }

    public String getNameAsAadhar() {
        return nameAsAadhar;
    }

    public void setNameAsAadhar(String nameAsAadhar) {
        this.nameAsAadhar = nameAsAadhar;
    }
}
