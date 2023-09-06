package com.example.spring.parse.service;

import com.example.spring.parse.data.FindWordDefinitionData;
import com.example.spring.parse.data.WordDefinitionData;
import com.example.spring.parse.repository.WordDefinitionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class WordDefinitionServiceImpl implements WordDefinitionService {

  @Autowired
  private ConversionService conversionService;


  @Autowired
  private WordDefinitionRepository wordDefinitionRepository;


  @Override
  public List<FindWordDefinitionData> getByNameContainingIgnoreCase(String word) {
    List<WordDefinitionData> byNameContainingIgnoreCase = findByNameContainingIgnoreCase(word);
    return byNameContainingIgnoreCase.stream()
        .map(wordDefinition -> conversionService.convert(wordDefinition, FindWordDefinitionData.class))
        .collect(
            Collectors.toList());
  }

  @Override
  public List<WordDefinitionData> findByNameContainingIgnoreCase(String word) {
    return wordDefinitionRepository.findByNameContainingIgnoreCase(word).stream()
        .map(wordDefinition -> conversionService.convert(wordDefinition, WordDefinitionData.class))
        .collect(
            Collectors.toList());
  }

}
