package com.example.spring.batch.config;

import com.example.spring.batch.config.tasklet.CopyFilesTasklet;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CopyCsvConfig {

  private StepBuilderFactory stepBuilderFactory;

  private CopyFilesTasklet copyFilesTasklet;

  @Bean
  public Step stepReturnInitialCopyFile() {
    return stepBuilderFactory.get("csv-copy-step")
        .tasklet(copyFilesTasklet)
        .build();
  }

}
