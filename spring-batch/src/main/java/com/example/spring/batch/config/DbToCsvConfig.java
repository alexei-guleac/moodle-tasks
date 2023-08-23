package com.example.spring.batch.config;

import com.example.spring.batch.entity.Customer;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class DbToCsvConfig {

  private static final String QUERY_FIND_CUSTOMERS =
      "SELECT customer_id, first_name, last_name, email, gender, contact, country, dob " +
          "FROM CUSTOMERS_INFO " +
          "ORDER BY customer_id ASC";

  private StepBuilderFactory stepBuilderFactory;

  private DataSource dataSource;

  @Bean
  public ItemReader<Customer> itemReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<Customer>()
        .name("cursorItemReader")
        .dataSource(dataSource)
        .sql(QUERY_FIND_CUSTOMERS)
        .rowMapper(new BeanPropertyRowMapper<>(Customer.class))
        .build();
  }

  @Bean
  public FlatFileItemWriter<Customer> itemWriter() {
    return new FlatFileItemWriterBuilder<Customer>()
        .name("itemWriter")
        .resource(new FileSystemResource("src/main/resources/customer-filtered.csv"))
        .lineAggregator(new PassThroughLineAggregator<>())
        .build();
  }

  @Bean
  public Step stepShowUpdatedFile() {
    return stepBuilderFactory.get("csv-upd-step").<Customer, Customer>chunk(1)
        .reader(itemReader(dataSource))
        .writer(itemWriter())
        .build();
  }

}
