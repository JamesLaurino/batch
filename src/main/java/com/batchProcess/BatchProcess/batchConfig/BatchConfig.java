package com.batchProcess.BatchProcess.batchConfig;

import com.batchProcess.BatchProcess.dto.ClientDto;
import com.batchProcess.BatchProcess.entity.Client;
import com.batchProcess.BatchProcess.processor.ClientProcessor;
import com.batchProcess.BatchProcess.processor.DataProcessor;
import com.batchProcess.BatchProcess.repository.ClientRepository;
import com.batchProcess.BatchProcess.writer.ClientWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BatchConfig
{
    @Value("${inputFile}")
    private Resource inputFile;

    @Autowired private JobRepository jobRepository;
    @Autowired private PlatformTransactionManager platformTransactionManager;
    @Autowired private ClientProcessor clientProcessor;
    @Autowired private DataProcessor dataProcessor;
    @Autowired private ClientWriter clientWriter;

    @Bean
    public FlatFileItemReader<ClientDto> reader() {
        FlatFileItemReader<ClientDto> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(inputFile);
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public Job runJob()
    {
        Step stepOne= new StepBuilder("csvImport", jobRepository)
                .<ClientDto, Client>chunk(1000, platformTransactionManager)
                .reader(reader())
                .processor(clientProcessor)
                .processor(dataProcessor)
                .writer(clientWriter)
                //.taskExecutor(taskExecutor())
                .build();

        return new JobBuilder("importStudents", jobRepository)
                .start(stepOne)
                .build();
    }

    private LineMapper<ClientDto> lineMapper() {
        DefaultLineMapper<ClientDto> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "annee");

        BeanWrapperFieldSetMapper<ClientDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ClientDto.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public ItemProcessor<ClientDto,Client> composite()
    {
        List<ItemProcessor<ClientDto,Client>> itemProcessorList= new ArrayList<>();
        itemProcessorList.add(clientProcessor);
        itemProcessorList.add(dataProcessor);

        CompositeItemProcessor<ClientDto,Client> compositeItemProcessor =  new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessorList);

        return compositeItemProcessor;
    }

    //    @Bean
    //    public TaskExecutor taskExecutor() {
    //        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
    //        asyncTaskExecutor.setConcurrencyLimit(10);
    //        return asyncTaskExecutor;
    //    }


    //    @Bean
    //    public Step step1() {
    //        return new StepBuilder("csvImport", jobRepository)
    //                .<Client, Client>chunk(1000, platformTransactionManager)
    //                .reader(reader())
    //                .processor(clientProcessor)
    //                .writer(writer())
    //                //.taskExecutor(taskExecutor())
    //                .build();
    //    }
}
