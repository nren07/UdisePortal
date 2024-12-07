package com.udise.portal.service.job.job_record_manager.impl;

import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.Category;
import com.udise.portal.enums.JobStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class Type3 {
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
                    System.out.println(cell.getStringCellValue());
                    headerMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
                }
            }
            while (rows.hasNext()) {
                try{
                    Row currentRow = rows.next();
                    JobRecord record = new JobRecord();

                    if (headerMap.containsKey("student name")) {
                        Cell cell=currentRow.getCell(headerMap.get("student name"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setStudentName(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("section")) {
                        Cell cell=currentRow.getCell(headerMap.get("section"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setSection(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("class")) {
                        Cell cell=currentRow.getCell(headerMap.get("class"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setClassName(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("student pen")) {
                        Cell cell=currentRow.getCell(headerMap.get("student pen"));
                        if(cell!=null && cell.getCellType()==CellType.NUMERIC){
                            record.setStudentPen(cell.getNumericCellValue());

                        }else if(cell!=null && cell.getCellType()==CellType.STRING){
                            try{
                                record.setStudentPen(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("student pen is not valid");
                            }
                        }else{
                            log.error("student pen is not present");
                        }
                    }

                    if (headerMap.containsKey("Name As per AADHAAR")) {
                        Cell cell=currentRow.getCell(headerMap.get("Name As per AADHAAR"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            record.setNameAsAadhar(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("4.2.8no. of days student attended school (in the previous academic year)")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.2.8no. of days student attended school (in the previous academic year)"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setAttendance(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()== CellType.STRING){
                            try{
                                record.setAttendance(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("Student Attendence is not valid");
                            }
                        }else{
                            log.error("Student Attendence is not present");
                        }
                    }

                    if (headerMap.containsKey("4.2.7(b) in the previous class studied – marks obtained (in percentage)")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.2.7(b) in the previous class studied – marks obtained (in percentage)"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setPercentage(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()== CellType.STRING){
                            try{
                                record.setPercentage(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("student percentage is not valid");
                            }
                        }else{
                            log.error("student percentage is not present");
                        }
                    }

                    if (headerMap.containsKey("gender")) {
                        Cell cell=currentRow.getCell(headerMap.get("gender"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setGender(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("date of birth")) {
                        Cell cell=currentRow.getCell(headerMap.get("date of birth"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setDob(cell.getDateCellValue());
                        }

                    }

                    if (headerMap.containsKey("student state code")) {
                        Cell cell=currentRow.getCell(headerMap.get("student state code"));
                        if(cell!=null && cell.getCellType()==CellType.NUMERIC){
                            record.setStateCode(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()==CellType.STRING){
                            try{
                                record.setStateCode(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("Student state code not valid");
                            }
                        }else{
                            log.info("Student State code not present");
                        }
                    }

                    if (headerMap.containsKey("mother's name")) {
                        Cell cell=currentRow.getCell(headerMap.get("mother's name"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            record.setMotherName(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("father's name")) {
                        Cell cell=currentRow.getCell(headerMap.get("father's name"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            record.setFatherName(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("aadhar number")) {
                        Cell cell=currentRow.getCell(headerMap.get("aadhar number"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            if(!cell.getStringCellValue().contains("NA")){
                                record.setAadharNumber(cell.getStringCellValue());
                            }
                        }else if(cell!=null && cell.getCellType()==CellType.NUMERIC){
                            record.setAadharNumber(String.valueOf(cell.getNumericCellValue()));
                        }else {
                            log.info("Student aadhar no not present");
                        }
                    }

                    if (headerMap.containsKey("name as per aadhar")) {
                        Cell cell=currentRow.getCell(headerMap.get("name as per aadhar"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            record.setNameAsAadhar(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("date of admission")) {
                        Cell cell=currentRow.getCell(headerMap.get("date of admission"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setDateOfAdmission(cell.getDateCellValue());
                        }
                    }

                    if (headerMap.containsKey("address")) {
                        Cell cell=currentRow.getCell(headerMap.get("address"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setAddress(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("pin code")) {
                        Cell cell=currentRow.getCell(headerMap.get("pin code"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setPinCode(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()== CellType.STRING){
                            try{
                                record.setPinCode(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("Student pin code not correct format");
                            }
                        }else{
                            log.error("student pin code not present");
                        }
                    }

                    if (headerMap.containsKey("father's mobile no")) {
                        Cell cell=currentRow.getCell(headerMap.get("father's mobile no"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setFatherMoNumber(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()== CellType.STRING){
                            try{
                                record.setFatherMoNumber(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("Mobile no is not valid");
                            }
                        }else{
                            log.error("mobile not not present");
                        }
                    }

                    if (headerMap.containsKey("mother tongue of the student")) {
                        Cell cell=currentRow.getCell(headerMap.get("mother tongue of the student"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setMotherTongue(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("category")) {
                        Cell cell=currentRow.getCell(headerMap.get("category"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            String category=cell.getStringCellValue();
                            if(category.toLowerCase().contains("gen")){
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
                        }else{
                            log.error("category not present");
                        }
                    }

                    if (headerMap.containsKey("minority group")) {
                        Cell cell=currentRow.getCell(headerMap.get("minority group"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            String minority=cell.getStringCellValue();
                            if(minority.contains("Muslim")) record.setMinorityGroup("1-Muslim");
                            if(minority.contains("Christian")) record.setMinorityGroup("2-Christian");
                            if(minority.contains("Sikh")) record.setMinorityGroup("3-Sikh");
                            if(minority.contains("Buddhist")) record.setMinorityGroup("4-Buddhist");
                            if(minority.contains("Parsi")) record.setMinorityGroup("5-Parsi");
                            if(minority.contains("Jain")) record.setMinorityGroup("6-Jain");
                            if(minority.contains("NA")) record.setMinorityGroup("7-NA");
                        }
                    }

                    if (headerMap.containsKey("whether bpl beneficiary?")) {
                        Cell cell=currentRow.getCell(headerMap.get("whether bpl beneficiary?"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            String bplValue = cell.getStringCellValue().trim();
                            boolean isBpl= !(bplValue.equalsIgnoreCase("No") || bplValue.equalsIgnoreCase("NO") || bplValue.equalsIgnoreCase("NA"));
                            record.setBpl(isBpl);
                        }else continue;
                    }

                    if (headerMap.containsKey("whether belongs to ews / disadvantaged group?")) {
                        Cell cell=currentRow.getCell(headerMap.get("whether belongs to ews / disadvantaged group?"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            String ewsValue = cell.getStringCellValue().trim();
                            boolean isEws= !(ewsValue.equalsIgnoreCase("No") || ewsValue.equalsIgnoreCase("NO") || ewsValue.equalsIgnoreCase("NA"));
                            record.setEws(isEws);
                        }else if(cell!=null && cell.getCellType()== CellType.BOOLEAN){
                            boolean isEws = cell.getBooleanCellValue();
                            record.setEws(isEws);
                        }else{
                            log.error("invalid value");
                        }
                    }

                    if (headerMap.containsKey("whether cwsn?")) {
                        Cell cell=currentRow.getCell(headerMap.get("whether cwsn?"));
                        if(cell!=null && cell.getCellType()==CellType.STRING){
                            String cswnValue = cell.getStringCellValue().trim();
                            boolean isCSWN= !(cswnValue.equalsIgnoreCase("No") || cswnValue.equalsIgnoreCase("NO") || cswnValue.equalsIgnoreCase("NA"));
                            record.setCwsn(isCSWN);
                        }else if(cell!=null && cell.getCellType()== CellType.BOOLEAN){
                            boolean isCSWN = cell.getBooleanCellValue();
                            record.setCwsn(isCSWN);
                        }else{
                            log.error("invalid value");
                        }
                    }

                    if (headerMap.containsKey("is this student identified as out-of-school-child in current or previous years?")) {
                        Cell cell=currentRow.getCell(headerMap.get("is this student identified as out-of-school-child in current or previous years?"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            String ooscValue = cell.getStringCellValue().trim();
                            boolean isOosc= !(ooscValue.equalsIgnoreCase("No") || ooscValue.equalsIgnoreCase("NO") || ooscValue.equalsIgnoreCase("NA"));
                            record.setOosc(isOosc);
                        }else if(cell!=null && cell.getCellType()== CellType.BOOLEAN){
                            boolean isOosc = cell.getBooleanCellValue();
                            record.setOosc(isOosc);
                        }else{
                            log.error("invalid value");
                        }
                    }

                    if (headerMap.containsKey("blood group")) {
                        Cell cell=currentRow.getCell(headerMap.get("blood group"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setBloodGroup(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("admission number")) {
                        Cell cell=currentRow.getCell(headerMap.get("admission number"));
                        if(cell!=null && cell.getCellType()==CellType.NUMERIC){
                            record.setAdmissionNumber(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()==CellType.STRING){
                            try{
                                record.setAdmissionNumber(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e) {
                                log.error("Admission no is not valid");
                            }
                        }else{
                            log.error("Admission no is not Present");
                        }
                    }

                    if (headerMap.containsKey("4.2.5(a) status of student in previous academic year of schooling")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.2.5(a) status of student in previous academic year of schooling"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setStatusOfStudentPrevAcademic(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("4.2.5(b) grade/class studied in the previous/last academic year")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.2.5(b) grade/class studied in the previous/last academic year"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setClassStudiedInPreviousAcademicYear(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("4.3.6has the student been identified as a gifted / talented?")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.3.6has the student been identified as a gifted / talented?"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            String giftedValue = cell.getStringCellValue().trim();
                            boolean isGifted= !(giftedValue.equalsIgnoreCase("No") || giftedValue.equalsIgnoreCase("NO") || giftedValue.equalsIgnoreCase("NA"));
                            record.setGifted(isGifted);
                        }else if(cell!=null && cell.getCellType()== CellType.BOOLEAN){
                            boolean isGifted = cell.getBooleanCellValue();
                            record.setGifted(isGifted);
                        }else{
                            log.error("invalid value");
                        }
                    }

                    if (headerMap.containsKey("4.2.6admitted / enrolled under (only for pvt. unaided)")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.2.6admitted / enrolled under (only for pvt. unaided)"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setEnrolledUnder(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("4.2.7(a) in the previous class studied – result of the examination")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.2.7(a) in the previous class studied – result of the examination"));
                        if(cell!=null && cell.getCellType()== CellType.STRING){
                            record.setResultOfExamination(cell.getStringCellValue());
                        }
                    }

                    if (headerMap.containsKey("4.3.10(a) student's height (in cms)")) {
                        Cell cell=currentRow.getCell(headerMap.get("4.3.10(a) student's height (in cms)"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setHeight(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()== CellType.STRING){
                            try{
                                record.setHeight(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("height is not valid");
                            }
                        }else{
                            log.error("height is not present");
                        }
                    }

                    if (headerMap.containsKey("(b) student's weight (in kgs)")) {
                        Cell cell=currentRow.getCell(headerMap.get("(b) student's weight (in kgs)"));
                        if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                            record.setWeight(cell.getNumericCellValue());
                        }else if(cell!=null && cell.getCellType()== CellType.STRING){
                            try{
                                record.setWeight(Double.parseDouble(cell.getStringCellValue()));
                            }catch (Exception e){
                                log.error("Weight is not valid");
                            }
                        }else{
                            log.error("Weight is not present");
                        }
                    }

                    record.setJob(job);
                    record.setJobStatus(JobStatus.PENDING);
                    jobRecordDao.save(record);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error is in job upload"+e.getMessage());
        }
    }
}
