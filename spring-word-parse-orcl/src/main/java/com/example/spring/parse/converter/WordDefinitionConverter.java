package com.example.spring.parse.converter;

import com.example.spring.parse.data.WordDefinitionData;
import com.example.spring.parse.entity.WordDefinition;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WordDefinitionConverter implements Converter<WordDefinition, WordDefinitionData> {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public WordDefinitionData convert(WordDefinition source) {

    String commaSeparator = ",";

    String synonyms = source.getSynonyms();
    if (synonyms == null) {
      synonyms = "";
    }
    String antonyms = source.getAntonyms();
    if (antonyms == null) {
      antonyms = "";
    }
    String relatives = source.getRelatives();
    if (relatives == null) {
      relatives = "";
    }

    return WordDefinitionData.builder()
        .active(source.getActive())
        .created_at(source.getCreated_at().format(dateTimeFormatter))
        .dictionary_id(source.getDictionary_id())
        .user_id(source.getUser_id())
        .definition(source.getDefinition())
        .predefinition(source.getPredefinition())
        .name(source.getName())
        .synonyms(new HashSet<>(Arrays.asList(synonyms.split(commaSeparator))))
        .antonyms(new HashSet<>(Arrays.asList(antonyms.split(commaSeparator))))
        .relatives(new HashSet<>(Arrays.asList(relatives.split(commaSeparator))))
        .build();
  }
}
