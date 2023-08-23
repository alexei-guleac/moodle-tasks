package com.example.spring.batch.config;

import com.example.spring.batch.config.tasklet.MoveFilesTasklet;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class MoveCsvConfig {

  private StepBuilderFactory stepBuilderFactory;

  private MoveFilesTasklet moveFilesTasklet;

  @Bean
  public Step stepMoveFile() {
    return stepBuilderFactory.get("csv-move-step")
        .tasklet(moveFilesTasklet)
        .build();
  }

}
