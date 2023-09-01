package com.example.spring.batch.config.batch.tasklet;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class MoveFilesTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {

    final File directory = new File("src/main/resources/");
    final File director0 = new File("src/main/resources");
    final File directory21 = new File("./");

    Arrays.asList(
        Objects.requireNonNull(
            directory.listFiles((dir, name) -> name.matches("customers.*?"))))
        .forEach(singleFile -> singleFile.renameTo(new File("src/main/customers.csv")));
    return RepeatStatus.FINISHED;

  }

}
