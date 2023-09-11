package com.example.spring.parse.service;

import com.example.spring.parse.service.parser.ResourceHtmlParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobLauncher {

  @Autowired
  private ResourceHtmlParser resourceHtmlParser;

  @PostConstruct
  public void runFileProcessing() throws JsonProcessingException {
//    resourceHtmlParser.parseHtmlToJson();
  }

}
