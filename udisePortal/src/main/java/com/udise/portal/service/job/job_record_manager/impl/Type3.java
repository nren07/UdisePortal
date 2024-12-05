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
                Row currentRow = rows.next();
                JobRecord record = new JobRecord();
                if (headerMap.containsKey("student name")) {
                    Cell cell=currentRow.getCell(headerMap.get("student name"));
                    if(cell!=null && cell.getCellType()== CellType.STRING){
                        record.setStudentName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("section")) {
                    Cell cell=currentRow.getCell(headerMap.get("section"));
                    if(cell!=null && cell.getCellType()== CellType.STRING){
                        record.setSection(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("class")) {
                    Cell cell=currentRow.getCell(headerMap.get("class"));
                    if(cell!=null && cell.getCellType()== CellType.STRING){
                        record.setClassName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("student pen")) {
                    Cell cell=currentRow.getCell(headerMap.get("student pen"));
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
                if (headerMap.containsKey("4.2.8no. of days student attended school (in the previous academic year)")) {
                    Cell cell=currentRow.getCell(headerMap.get("4.2.8no. of days student attended school (in the previous academic year)"));
                    if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                        record.setAttendance(cell.getNumericCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("4.2.7(b) in the previous class studied – marks obtained (in percentage)")) {
                    Cell cell=currentRow.getCell(headerMap.get("4.2.7(b) in the previous class studied – marks obtained (in percentage)"));
                    if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                        record.setPercentage(cell.getNumericCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("gender")) {
                    Cell cell=currentRow.getCell(headerMap.get("gender"));
                    if(cell!=null && cell.getCellType()== CellType.STRING){
                        record.setGender(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("date of birth")) {
                    Cell cell=currentRow.getCell(headerMap.get("date of birth"));
                    if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                        record.setDob(cell.getDateCellValue());
                    }else continue;

                }
                if (headerMap.containsKey("student state code")) {
                    Cell cell=currentRow.getCell(headerMap.get("student state code"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        try{
                            record.setStateCode(Double.parseDouble(cell.getStringCellValue()));
                        }catch (Exception e){
                            System.out.println("State code error for"+record.getStudentName());
                            log.error(e);
                        }
                    }
                }
                if (headerMap.containsKey("mother's name")) {
                    Cell cell=currentRow.getCell(headerMap.get("mother's name"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setMotherName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("father's name")) {
                    Cell cell=currentRow.getCell(headerMap.get("father's name"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setFatherName(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("aadhar number")) {
                    Cell cell=currentRow.getCell(headerMap.get("aadhar number"));
                    if(cell!=null && cell.getCellType()==CellType.STRING){
                        record.setAadharNumber(cell.getStringCellValue());
                    }else continue;
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
                    }else continue;
                }
                if (headerMap.containsKey("address ")) {
                    Cell cell=currentRow.getCell(headerMap.get("address"));
                    if(cell!=null && cell.getCellType()== CellType.STRING){
                        record.setAddress(cell.getStringCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("pin code")) {
                    Cell cell=currentRow.getCell(headerMap.get("pin code"));
                    if(cell!=null && cell.getCellType()== CellType.NUMERIC){
                        record.setPinCode(cell.getNumericCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("father's mobile no")) {
                    Cell cell=currentRow.getCell(headerMap.get("father's mobile no"));
                    if(cell!=null && cell.getCellType()== CellType.STRING){
                        record.setFatherMoNumber(cell.getNumericCellValue());
                    }else continue;
                }
                if (headerMap.containsKey("mother tongue of the student")) {
                    record.setMotherTongue(currentRow.getCell(headerMap.get("mother tongue of the student")).getStringCellValue());
                }
                if (headerMap.containsKey("category")) {
                    String category=currentRow.getCell(headerMap.get("category")).getStringCellValue();

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
                if (headerMap.containsKey("minority group")) {
                    record.setMinorityGroup(currentRow.getCell(headerMap.get("minority group")).getStringCellValue());
                }
                if (headerMap.containsKey("whether bpl beneficiary?")) {
                    Boolean isBpl= !currentRow.getCell(headerMap.get("whether bpl beneficiary?")).getStringCellValue().equals("NO");
                    record.setBpl(isBpl);
                }
                if (headerMap.containsKey("whether belongs to ews / disadvantaged group?")) {
                    String ewsValue = currentRow.getCell(headerMap.get("whether belongs to ews / disadvantaged group?")).getStringCellValue().trim();
                    boolean isEws = !ewsValue.equalsIgnoreCase("NO");
                    record.setEws(isEws);
                }
                if (headerMap.containsKey("whether cwsn?")) {
                    String cwsnValue = currentRow.getCell(headerMap.get("whether cwsn?")).getStringCellValue().trim();
                    boolean isCwsn = !cwsnValue.equalsIgnoreCase("NO");
                    record.setCwsn(isCwsn);
                }
                if (headerMap.containsKey("is this student identified as out-of-school-child in current or previous years?")) {
                    String ooscValue = currentRow.getCell(headerMap.get("is this student identified as out-of-school-child in current or previous years?")).getStringCellValue().trim();
                    boolean isOosc = !ooscValue.equalsIgnoreCase("NO");
                    record.setOosc(isOosc);
                }
                if (headerMap.containsKey("blood group")) {
                    record.setBloodGroup(currentRow.getCell(headerMap.get("blood group")).getStringCellValue());
                }
                if (headerMap.containsKey("admission number")) {
                    record.setAdmissionNumber(currentRow.getCell(headerMap.get("admission number")).getNumericCellValue());
                }
                if (headerMap.containsKey("4.2.5(a) status of student in previous academic year of schooling")) {
                    record.setStatusOfStudentPrevAcademic(currentRow.getCell(headerMap.get("4.2.5(a) status of student in previous academic year of schooling")).getStringCellValue());
                }
                if (headerMap.containsKey("4.2.5(b) grade/class studied in the previous/last academic year")) {
                    System.out.println("class studied in prev academic year"+currentRow.getCell(headerMap.get("4.2.5(b) grade/class studied in the previous/last academic year")).getStringCellValue());
                    record.setClassStudiedInPreviousAcademicYear(currentRow.getCell(headerMap.get("4.2.5(b) grade/class studied in the previous/last academic year")).getStringCellValue());
                }
                if (headerMap.containsKey("4.3.6has the student been identified as a gifted / talented?")) {
                    String giftedValue = currentRow.getCell(headerMap.get("4.3.6has the student been identified as a gifted / talented?")).getStringCellValue().trim();
                    boolean isGifted = !giftedValue.equalsIgnoreCase("NO");
                    record.setGifted(isGifted);
                }
                if (headerMap.containsKey("4.2.6admitted / enrolled under (only for pvt. unaided)")) {
                    record.setEnrolledUnder(currentRow.getCell(headerMap.get("4.2.6admitted / enrolled under (only for pvt. unaided)")).getStringCellValue());
                }
                if (headerMap.containsKey("4.2.7(a) in the previous class studied – result of the examination")) {
                    String classStudied=currentRow.getCell(headerMap.get("4.2.7(a) in the previous class studied – result of the examination")).getStringCellValue();
                    record.setResultOfExamination(classStudied);
                }
                if (headerMap.containsKey("4.3.10(a) student's height (in cms)")) {
                    record.setHeight(currentRow.getCell(headerMap.get("4.3.10(a) student's height (in cms)")).getNumericCellValue());
                }

                if (headerMap.containsKey("(b) student's weight (in kgs)")) {
                    record.setWeight(currentRow.getCell(headerMap.get("(b) student's weight (in kgs)")).getNumericCellValue());
                }
                record.setJob(job);
                record.setJobStatus(JobStatus.PENDING);
                jobRecordDao.save(record);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error is in job upload"+e.getMessage());
        }
    }
}
