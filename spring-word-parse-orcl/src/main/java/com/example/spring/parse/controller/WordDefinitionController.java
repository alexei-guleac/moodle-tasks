package com.example.spring.parse.controller;


import com.example.spring.parse.data.FindWordDefinitionData;
import com.example.spring.parse.data.GetWordDefinitionData;
import com.example.spring.parse.service.HtmlParser;
import com.example.spring.parse.service.WordDefinitionService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/wd")
public class WordDefinitionController {

  @Autowired
  private WordDefinitionService definitionService;

  @Autowired
  private HtmlParser htmlParser;


  @PostMapping("/findWordDefinition")
  public List<FindWordDefinitionData> findWordDefinition(
      @RequestBody GetWordDefinitionData getWordDefinitionData) {
    return definitionService.getByNameContainingIgnoreCase(getWordDefinitionData.getWord());
  }

  @PostMapping("/uploadWordDefinition")
  public void uploadWordDefinition(
      @RequestParam("file") MultipartFile file) throws IOException {
     htmlParser.parseHtmlToJson(file);
  }
}
