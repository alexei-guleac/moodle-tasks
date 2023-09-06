package com.example.spring.parse.service;

import com.example.spring.parse.data.FindWordDefinitionData;
import com.example.spring.parse.data.GetWordDefinitionData;
import com.example.spring.parse.data.WordDefinitionData;
import com.example.spring.parse.entity.WordDefinition;
import java.util.List;

public interface WordDefinitionService {

  List<FindWordDefinitionData> getByNameContainingIgnoreCase(String word);

  List<WordDefinitionData> findByNameContainingIgnoreCase(String word);
}
