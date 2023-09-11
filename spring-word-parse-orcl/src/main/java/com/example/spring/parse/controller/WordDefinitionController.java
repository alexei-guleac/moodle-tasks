package com.example.spring.parse.controller;


import com.example.spring.parse.data.FindWordDefinitionData;
import com.example.spring.parse.data.GetWordDefinitionData;
import com.example.spring.parse.service.parser.HtmlParser;
import com.example.spring.parse.service.WordDefinitionService;
import com.example.spring.parse.service.parser.WordParser;
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

  @Autowired
  private WordParser wordParser;


  @PostMapping("/findWordDefinition")
  public List<FindWordDefinitionData> findWordDefinition(
      @RequestBody GetWordDefinitionData getWordDefinitionData) {
    return definitionService.getByNameContainingIgnoreCase(getWordDefinitionData.getWord());
  }

  @PostMapping("/uploadHtmlWordDefinition")
  public void uploadHtmlWordDefinition(
      @RequestParam("file") MultipartFile file) throws IOException {
     htmlParser.parseHtml(file);
  }

  @PostMapping("/uploadWordDefinition")
  public void uploadWordDefinition(
      @RequestParam("file") MultipartFile file) throws IOException {
    wordParser.parseWord(file);
  }
}
