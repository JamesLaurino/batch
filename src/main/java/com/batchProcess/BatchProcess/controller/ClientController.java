package com.batchProcess.BatchProcess.controller;

import com.batchProcess.BatchProcess.processor.DataProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Autowired
    private DataProcessor dataProcessor;

    @GetMapping("/datas")
    public Map<String,Integer> getData()
    {
        Map<String, Integer> map= new HashMap<>();
        map.put("Mineur", dataProcessor.mineurQty);
        map.put("Majeur", dataProcessor.majeurQty);

        return map;
    }

    @GetMapping("/start")
    public String importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try
        {
            jobLauncher.run(job, jobParameters);
            return "COMPLETE";
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e)
        {
            e.printStackTrace();
            return "ERROR BATCH";
        }
    }
}
