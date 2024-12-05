package com.udise.portal.service.job.job_record_manager.impl;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.Category;
import com.udise.portal.enums.JobStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.atp.Switch;
import org.apache.poi.ss.usermodel.*;
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
                    Cell cell=currentRow.getCell(headerMap.get("Name"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setStudentName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("Section")) {
                    Cell cell=currentRow.getCell(headerMap.get("Section"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setSection(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("Class")) {
                    Cell cell=currentRow.getCell(headerMap.get("Class"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setClassName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("Student PEN")) {
                    Cell cell=currentRow.getCell(headerMap.get("Student PEN"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        try{
                            record.setStudentPen(Double.parseDouble(cell.getStringCellValue()));
                        }catch (Exception e){
                            System.out.println("Student pen Error "+record.getStudentName());
                            log.error("student pen is not valid");
                            continue;
                        }
                    }else continue;
                }
                if (headerMap.containsKey("Gender")) {
                    Cell cell=currentRow.getCell(headerMap.get("Gender"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setGender(cell.getStringCellValue());
                    }
                }
                if (headerMap.containsKey("Student State Code")) {
                    Cell cell=currentRow.getCell(headerMap.get("Student State Code"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        try{
                            record.setStateCode(Double.parseDouble(cell.getStringCellValue()));
                        }catch (Exception e){
                            System.out.println("State code error for"+record.getStudentName());
                            log.error(e);
                        }
                    }
                }
                if (headerMap.containsKey("Mother Name")) {
                    Cell cell=currentRow.getCell(headerMap.get("Mother Name"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setMotherName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("Father Name")) {
                    Cell cell=currentRow.getCell(headerMap.get("Father Name"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setFatherName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("AADHAAR No.")) {
                    Cell cell=currentRow.getCell(headerMap.get("AADHAAR No."));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setAadharNumber(cell.getStringCellValue());
                    }
                }
                if (headerMap.containsKey("Name As per AADHAAR")) {
                    Cell cell=currentRow.getCell(headerMap.get("Name As per AADHAAR"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setNameAsAadhar(cell.getStringCellValue());
                    }
                }
                if (headerMap.containsKey("Social Category")) {
                    Cell cell=currentRow.getCell(headerMap.get("Social Category"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        String category=cell.getStringCellValue();
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
                    }else continue;
                }
                if (headerMap.containsKey("Minority Group")) {
                    Cell cell=currentRow.getCell(headerMap.get("Social Category"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        String minority=cell.getStringCellValue();
                        if(minority.contains("Muslim")) record.setMinorityGroup("1-Muslim");
                        if(minority.contains("Christian")) record.setMinorityGroup("2-Christian");
                        if(minority.contains("Sikh")) record.setMinorityGroup("3-Sikh");
                        if(minority.contains("Buddhist")) record.setMinorityGroup("4-Buddhist");
                        if(minority.contains("Parsi")) record.setMinorityGroup("5-Parsi");
                        if(minority.contains("Jain")) record.setMinorityGroup("6-Jain");
                        if(minority.contains("NA")) record.setMinorityGroup("7-NA");
                    }else continue;
                }
                if (headerMap.containsKey("BPL beneficiary")) {
                    Cell cell=currentRow.getCell(headerMap.get("BPL beneficiary"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        String bplValue = cell.getStringCellValue().trim();
                        boolean isBpl= !(bplValue.equalsIgnoreCase("No") || bplValue.equalsIgnoreCase("NA"));
                        record.setBpl(isBpl);
                    }else continue;
                }
                if (headerMap.containsKey("CWSN")) {
                    Cell cell=currentRow.getCell(headerMap.get("BPL beneficiary"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        String cswnValue = cell.getStringCellValue().trim();
                        boolean isCSWN= !(cswnValue.equalsIgnoreCase("No") || cswnValue.equalsIgnoreCase("NA"));
                        record.setCwsn(isCSWN);
                    }else continue;
                }
                if (headerMap.containsKey("Admission No.")) {
                    Cell cell=currentRow.getCell(headerMap.get("Admission No."));
                    if(cell!=null && cell.getCellType()==CellType.NUMERIC){
                        try{
                            double admissionNo=cell.getNumericCellValue();
                            record.setAdmissionNumber(admissionNo);
                        }catch (Exception e){
                            System.out.println("Admission No error");
                            log.error(e);
                            continue;
                        }
                    }else continue;
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

