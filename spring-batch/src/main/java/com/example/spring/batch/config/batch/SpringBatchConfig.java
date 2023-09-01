package com.example.spring.batch.config.batch;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

  private JobBuilderFactory jobBuilderFactory;

  private Step stepProcessFile;

  private Step stepMoveFile;

  private Step stepReturnInitialCopyFile;

  private Step stepShowUpdatedFile;


  @Bean
  public Job runJob() {
    return jobBuilderFactory.get("importCustomers")
        .flow(stepProcessFile)
        .next(stepMoveFile)
        .next(stepReturnInitialCopyFile)
        .next(stepShowUpdatedFile)
        .end().build();

  }

}
