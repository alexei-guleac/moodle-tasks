package com.example.spring.parse.converter;

import com.example.spring.parse.data.WordDefinitionData;
import com.example.spring.parse.entity.WordDefinition;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WordDefinitionDataConverter implements Converter<WordDefinitionData, WordDefinition> {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public WordDefinition convert(WordDefinitionData source) {

    String commaSeparator = ",";

    return WordDefinition.builder()
        .active(source.getActive())
        .created_at(LocalDateTime.parse(source.getCreated_at(), dateTimeFormatter))
        .dictionary_id(source.getDictionary_id())
        .user_id(source.getUser_id())
        .definition(Arrays.asList(source.getDefinition().split("\\s+")))
        .predefinition(source.getPredefinition())
        .name(source.getName())
        .synonyms(String.join(commaSeparator, source.getSynonyms()))
        .antonyms(String.join(commaSeparator, source.getAntonyms()))
        .relatives(String.join(commaSeparator, source.getRelatives()))
        .build();
  }
}
