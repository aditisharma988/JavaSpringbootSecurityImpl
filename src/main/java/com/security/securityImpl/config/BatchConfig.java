package com.security.securityImpl.config;

import com.security.securityImpl.entity.csv.IndustryData;
import com.security.securityImpl.repository.csv.IndustryDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class BatchConfig {

    private final IndustryDataRepository industryDataRepository;

    public FlatFileItemReader<IndustryData> itemReader() {

        FlatFileItemReader<IndustryData> itemReader = new FlatFileItemReader<>();
        //path set
        itemReader.setResource(new FileSystemResource("src/main/resources/industry-data.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;

    }

    private LineMapper<IndustryData> lineMapper() {

        DefaultLineMapper<IndustryData> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("year", "industry_code_ANZSIC", "industry_name_ANZSIC", "rme_size_grp", "variable", "value", "unit");
        tokenizer.setStrict(false);

        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(IndustryData.class);

        lineMapper.setFieldSetMapper(mapper);
        lineMapper.setLineTokenizer(tokenizer);
        return lineMapper;
    }


    // step 2 - item processor
    @Bean
    public IndustryDataProcessor processor() {
        return new IndustryDataProcessor();
    }

    //step 3 - item writer
    @Bean
    public RepositoryItemWriter<IndustryData> itemWriter() {
        RepositoryItemWriter<IndustryData> itemWriter = new RepositoryItemWriter<>();
        itemWriter.setRepository(industryDataRepository);
        itemWriter.setMethodName("save");
        return itemWriter;

    }

    //step 4 - implement step so that to assign to job later on
    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("csv-step", jobRepository).<IndustryData, IndustryData>chunk(10, platformTransactionManager).reader(itemReader()).processor(processor()).writer(itemWriter()).build();

    }

    //step 5 - after step defining , need to assign that to job
    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("csv-job", jobRepository).flow(step(jobRepository, platformTransactionManager)).end().build();
    }


}
