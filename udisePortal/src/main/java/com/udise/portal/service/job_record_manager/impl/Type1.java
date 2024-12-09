package com.udise.portal.service.job_record_manager.impl;

import com.udise.portal.dao.JobRecordDao;
import com.udise.portal.entity.Job;
import com.udise.portal.entity.JobRecord;
import com.udise.portal.enums.JobStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class Type1 {
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
                    headerMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
                }
            }
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                JobRecord record = new JobRecord();
                if (headerMap.containsKey("name")) {
                    if(currentRow.getCell(headerMap.get("name")).getStringCellValue()==""){
//                            messagingTemplate.convertAndSend("/topic/"+job.getAppUser().getId(), new SocketResponseVo("Upload Alert", ""));
                        log.info("Uploaded Job Name field Empty");
                        break;
                    }
                    record.setStudentName(currentRow.getCell(headerMap.get("name")).getStringCellValue());
                }
                if (headerMap.containsKey("section")) {
                    if(currentRow.getCell(headerMap.get("section")).getStringCellValue()==""){
                        log.info("Uploaded Job section field Empty");
                        break;
                    }
                    record.setSection(currentRow.getCell(headerMap.get("section")).getStringCellValue());
                }
                if (headerMap.containsKey("class")) {
                    if(currentRow.getCell(headerMap.get("class")).getStringCellValue()==""){
                        log.info("Uploaded Job class field Empty");
                        break;
                    }
                    record.setClassName(currentRow.getCell(headerMap.get("class")).getStringCellValue());
                }
                if (headerMap.containsKey("pen")) {
                    record.setStudentPen(currentRow.getCell(headerMap.get("pen")).getNumericCellValue());
                }
                if (headerMap.containsKey("attendance")) {
                    record.setAttendance(currentRow.getCell(headerMap.get("attendance")).getNumericCellValue());
                }
                if (headerMap.containsKey("percentage")) {
                    record.setPercentage(currentRow.getCell(headerMap.get("percentage")).getNumericCellValue());
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
