package com.udise.portal.service.job.job_record_manager.impl;

import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.Category;
import com.udise.portal.enums.JobStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.atp.Switch;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class Type2 {
    private static final Logger log = LogManager.getLogger(Type1.class);
    @Autowired
    private JobRecordDao jobRecordDao;

    public void createJobRecord(Job job, MultipartFile file){
        try{
            InputStream fis = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            Map<String, Integer> headerMap = new HashMap<>();
            if (rows.hasNext()) {
                Row headerRow = rows.next();
                for (Cell cell : headerRow) {
                    headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
                }
            }
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                JobRecord record = new JobRecord();
                if (headerMap.containsKey("Name")) {
                    record.setStudentName(currentRow.getCell(headerMap.get("Name")).getStringCellValue());
                }
                if (headerMap.containsKey("Section")) {
                    record.setSection(currentRow.getCell(headerMap.get("Section")).getStringCellValue());
                }
                if (headerMap.containsKey("Class")) {
                    record.setClassName(currentRow.getCell(headerMap.get("Class")).getStringCellValue());
                }
                if (headerMap.containsKey("Student PEN")) {
                    try{
                        record.setStudentPen(Double.parseDouble(currentRow.getCell(headerMap.get("Student PEN")).getStringCellValue()));
                    }catch (Exception e){
                        log.info("student pen is :"+currentRow.getCell(headerMap.get("Student PEN")).getStringCellValue());
                        log.error("student pen is not valid");
                    }
                }
                if (headerMap.containsKey("Gender")) {
                    record.setGender(currentRow.getCell(headerMap.get("Gender")).getStringCellValue());
                }
                if (headerMap.containsKey("Student State Code")) {
                    try{
                        record.setStateCode(Double.parseDouble(currentRow.getCell(headerMap.get("Student State Code")).getStringCellValue()));
                    }catch (Exception e){
                        log.info("Student state code is : "+currentRow.getCell(headerMap.get("Student State Code")).getStringCellValue());
                        log.error("Student state code is not valid");
                    }
                }
                if (headerMap.containsKey("Mother Name")) {
                    record.setMotherName(currentRow.getCell(headerMap.get("Mother Name")).getStringCellValue());
                }
                if (headerMap.containsKey("Father Name")) {
                    record.setFatherName(currentRow.getCell(headerMap.get("Father Name")).getStringCellValue());
                }
//                if (headerMap.containsKey("AADHAAR No.")) {
//                    if(!currentRow.getCell(headerMap.get("AADHAAR No.")).getStringCellValue().equals("NOT AVAILABLE")){
//                        record.setAadharNumber(currentRow.getCell(headerMap.get("AADHAAR No.")).getStringCellValue());
//                    }
//                }
                if (headerMap.containsKey("Social Category")) {
                    String category=currentRow.getCell(headerMap.get("Social Category")).getStringCellValue();

                    if(category.toLowerCase().contains("general")){
                        record.setCategory(Category.General);
                    }
                    if(category.toLowerCase().contains("obc") ){
                        record.setCategory(Category.OBC);
                    }
                    if(category.toLowerCase().contains("st")){
                        record.setCategory(Category.ST);
                    }
                    if(category.toLowerCase().contains("sc")){
                        record.setCategory(Category.SC);
                    }
                }
                if (headerMap.containsKey("Minority Group")) {
                    String minority=currentRow.getCell(headerMap.get("Minority Group")).getStringCellValue();
                    if(minority.contains("Muslim"))record.setMinorityGroup("1-Muslim");
                    if(minority.contains("Christian"))record.setMinorityGroup("2-Christian");
                    if(minority.contains("Sikh"))record.setMinorityGroup("3-Sikh");
                    if(minority.contains("Buddhist"))record.setMinorityGroup("4-Buddhist");
                    if(minority.contains("Parsi"))record.setMinorityGroup("5-Parsi");
                    if(minority.contains("Jain"))record.setMinorityGroup("6-Jain");
                    if(minority.contains("NA"))record.setMinorityGroup("7-NA");

                }
                if (headerMap.containsKey("BPL beneficiary")) {
                    Boolean isBpl= !currentRow.getCell(headerMap.get("BPL beneficiary")).getStringCellValue().equals("No");
                    record.setBpl(isBpl);
                }
                if (headerMap.containsKey("CWSN")) {
                    String cwsnValue = currentRow.getCell(headerMap.get("CWSN")).getStringCellValue().trim();
                    boolean isCwsn = !cwsnValue.equalsIgnoreCase("No");
                    record.setCwsn(isCwsn);
                }
                if (headerMap.containsKey("Admission No.")) {
                    try{
                        String admissionNo=currentRow.getCell(headerMap.get("Admission No.")).getStringCellValue();
                        record.setAdmissionNumber(Double.parseDouble(admissionNo));
                    }catch (Exception e){
                        log.info("admission no is : {}", currentRow.getCell(headerMap.get("Admission No.")).getStringCellValue());
                        log.error("admission no is not present");
                    }
                }

                record.setJob(job);
                record.setJobStatus(JobStatus.PENDING);
                jobRecordDao.save(record);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
