package com.security.securityImpl.controller.csv;

import com.security.securityImpl.entity.csv.IndustryData;
import com.security.securityImpl.repository.csv.IndustryDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class IndustryDataBatchController {

    private final JobLauncher jobLauncher;

    private IndustryDataRepository industryDataRepository;


    private static final String FILE_PATH = System.getProperty("user.home") + "/Downloads/industry-data.csv";

    private final Job job;

    @GetMapping(value = "/startBatch")
    public BatchStatus startBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        return jobExecution.getStatus();

    }

    // Location to save CSV file in Downloads folder
    @GetMapping("/download/csv")
    public String exportCSV() {
        List<IndustryData> dataList = industryDataRepository.findAll(); // Fetch data from DB

        if (dataList.isEmpty()) {
            return "No data found in database!";
        }

        File file = new File(FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            // Writing header
            writer.append("Year,Industry Code,Industry Name,RME Size Group,Variable,Value,Unit\n");

            // Writing data rows
            for (IndustryData data : dataList) {
                writer.append((char) data.getYear()).append(",").append(data.getIndustryCodeANZSIC()).append(",").append(data.getIndustryNameANZSIC()).append(",").append(data.getRmeSizeGrp()).append(",").append(data.getVariable()).append(",").append(String.valueOf(data.getValue())).append(",").append(data.getUnit()).append("\n");
            }

            return "CSV exported successfully! Saved at: " + FILE_PATH;
        } catch (IOException e) {
            return "Error writing CSV file: " + e.getMessage();
        }
    }

}
