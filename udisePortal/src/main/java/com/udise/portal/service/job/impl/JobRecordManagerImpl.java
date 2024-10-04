package com.udise.portal.service.job.impl;

import com.udise.portal.entity.JobRecord;
import com.udise.portal.service.job.JobRecordManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class JobRecordManagerImpl implements JobRecordManager {
    public List<JobRecord> save(Long userId,File file)
        {
            List<JobRecord> records = new ArrayList<>();
            try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
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
                    if (headerMap.containsKey("firstName")) {
                        record.setFirstName(currentRow.getCell(headerMap.get("firstName")).getStringCellValue());
                    }
                    if (headerMap.containsKey("middleName")) {
                        record.setMiddleName(currentRow.getCell(headerMap.get("middleName")).getStringCellValue());
                    }
                    if (headerMap.containsKey("lastName")) {
                        record.setLastName(currentRow.getCell(headerMap.get("lastName")).getStringCellValue());
                    }

                    if (headerMap.containsKey("rollNo")) {
                        record.setRollNo(currentRow.getCell(headerMap.get("rollNo")).getStringCellValue());
                    }
//                    record.setJob();
                    records.add(record);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return records;
    }
}
