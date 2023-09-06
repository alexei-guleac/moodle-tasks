package com.example.spring.parse.converter;

import com.example.spring.parse.data.FindWordDefinitionData;
import com.example.spring.parse.data.WordDefinitionData;
import com.example.spring.parse.entity.WordDefinition;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FindDefinitionDataConverter implements Converter<WordDefinitionData, FindWordDefinitionData> {


  @Override
  public FindWordDefinitionData convert(WordDefinitionData source) {

    return FindWordDefinitionData.builder()
        .active(source.getActive())
        .created_at(source.getCreated_at())
        .dictionary_id(source.getDictionary_id())
        .name(source.getName())
        .definition(Jsoup.parse(source.getDefinition()).text())
        .synonyms(source.getSynonyms())
        .antonyms(source.getAntonyms())
        .relatives(source.getRelatives())
        .build();
  }
}
