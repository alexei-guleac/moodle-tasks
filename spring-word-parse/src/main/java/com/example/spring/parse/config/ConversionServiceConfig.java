package com.example.spring.parse.config;


import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class ConversionServiceConfig {

  @Bean
  public ConversionService conversionService(Set<Converter<?, ?>> converters) {
    ConversionServiceFactoryBean csfb = new ConversionServiceFactoryBean();
    csfb.setConverters(converters);
    csfb.afterPropertiesSet();
    return csfb.getObject();
  }
}
