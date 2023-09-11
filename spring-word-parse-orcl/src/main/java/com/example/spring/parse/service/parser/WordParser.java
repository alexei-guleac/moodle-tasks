package com.example.spring.parse.service.parser;

import static com.example.spring.parse.utils.StringUtils.removeLastCharacter;

import com.example.spring.parse.data.WordDefinitionData;
import com.example.spring.parse.entity.WordDefinition;
import com.example.spring.parse.repository.WordDefinitionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WordParser {


  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");

  @Autowired
  private WordDefinitionRepository wordDefinitionRepository;

  @Autowired
  private ConversionService conversionService;


  public void parseWord(MultipartFile file) throws IOException {

    // open and prepare file
    XWPFDocument doc = new XWPFDocument(file.getInputStream());

    List<XWPFParagraph> list = doc.getParagraphs();
    LinkedList<String> paragraphs = new LinkedList<>();
    for (XWPFParagraph paragraph : list) {
//      System.out.println(paragraph.getText());
      paragraphs.add(paragraph.getText());
    }

    paragraphs.remove(0);
    HashMap<String, String> definitions = new HashMap<>();
    HashMap<String, String> notFoundDefinitions = new HashMap<>();

    HashMap<Integer, String> splitters = getSplittersMap();

    ArrayList<WordDefinitionData> wordDefinitionData = new ArrayList<>();

    // parse each word paragraph to desired data structure
    paragraphs.forEach(paragraph -> {

      WordDefinitionData wordDefinition = getPreBuildWordDefinition();

      int opFound = 0;
      for (Map.Entry<Integer, String> splitter : splitters.entrySet()) {
//        System.out.println(splitter.getKey() + "-/-" + splitter.getValue());

        String splitterValue = splitter.getValue();
        if (paragraph.indexOf(splitterValue) > 0) {
          String name = paragraph.substring(0, paragraph.indexOf(splitterValue));
          String definitionRemainder = paragraph.substring(paragraph.indexOf(splitterValue));
          opFound = 1;

          String textName = Jsoup.parse(name).text();

          if (textName.charAt(textName.length() - 1) == ',') {
            textName = removeLastCharacter(textName);
          }

          wordDefinition.setName(textName);
          wordDefinition.setPredefinition(textName);
          String definition = definitionRemainder;
          wordDefinition.setDefinition(definition);

          String textDefinition = Jsoup.parse(definition).text();

          // add synonyms
          String synonymStart = "Sin\\.:";
          String synonym = "Sin.";
          String commaSeparator = ",";
          String emptyString = "";
          wordDefinition.setSynonyms(new HashSet<>());
          Set<String> synonyms = wordDefinition.getSynonyms();

          if (textDefinition.indexOf(synonym) > 0) {
            String s = getRelativesString(textDefinition, commaSeparator, emptyString, synonymStart,
                synonym, synonyms, "textDefinitionSyn ");

            if (s.indexOf(synonym) > 0) {
              String s2 = getRelativesString(s, commaSeparator, emptyString, synonymStart, synonym,
                  synonyms, "textDefinitionSyn2 ");

              if (s2.indexOf(synonym) > 0) {
                String s3 = getRelativesString(s2, commaSeparator, emptyString, synonymStart,
                    synonym,
                    synonyms, "textDefinitionSyn3 ");
              }
            }
          }

          // add antonyms
          String antonymStart = "Ant\\.:";
          String antonym = "Ant.";
          wordDefinition.setAntonyms(new HashSet<>());
          Set<String> antonyms = wordDefinition.getAntonyms();

          if (textDefinition.indexOf(antonym) > 0) {
            String s = getRelativesString(textDefinition, commaSeparator, emptyString, antonymStart,
                antonym, antonyms, "textDefinitionAnt ");

            if (s.indexOf(antonym) > 0) {
              String s2 = getRelativesString(s, commaSeparator, emptyString, antonymStart, antonym,
                  antonyms, "textDefinitionAnt2 ");

              if (s2.indexOf(antonym) > 0) {
                String s3 = getRelativesString(s2, commaSeparator, emptyString, antonymStart,
                    antonym,
                    antonyms, "textDefinitionAnt3 ");
              }
            }
          }

          // add related words
          wordDefinition.setRelatives(new HashSet<>());
          String[] split = textName.split(commaSeparator);
          Set<String> relatives = wordDefinition.getRelatives();
          if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
              if (split[i].trim().length() > 0) {
                relatives.add(split[i]);
              }
            }
          }

          relatives.removeIf(s -> s.trim().equalsIgnoreCase("("));
          relatives.removeIf(s -> s.trim().equalsIgnoreCase(")"));

          wordDefinitionData.add(wordDefinition);
          definitions.put(name, definitionRemainder);
          break;
        }

      }
      if (opFound == 0) {
        notFoundDefinitions.put(paragraph, paragraph);
      }
    });

    System.out.println("notFoundDefinitions: " + notFoundDefinitions.size());
    notFoundDefinitions.forEach((s, s2) -> System.out.println(s2));
    System.out.println(wordDefinitionData.size());

    // write word data list to database
    wordDefinitionData.forEach(wordDefinition -> {
      wordDefinitionRepository.save(
          Objects.requireNonNull(conversionService.convert(wordDefinition, WordDefinition.class)));
    });

    System.out.println(definitions.size());

    // write word data list to json file
    writeListToJsonFile(wordDefinitionData);

  }

  private String getRelativesString(String textDefinition, String commaSeparator,
      String emptyString,
      String targetRelStart, String relative, Set<String> relativeList, String s4) {

    String textDefinitionRelStart = textDefinition.substring(textDefinition.indexOf(relative));
//    System.out.println(s4 + textDefinitionRelStart);
    String clearedString = textDefinitionRelStart.replaceFirst(targetRelStart, emptyString);
    try {
      String textRel = clearedString
          .substring(0, clearedString.indexOf("."));
//    System.out.println(Arrays.toString(textRel.split(commaSeparator)));

      relativeList.addAll(Arrays.asList(textRel.split(commaSeparator)));
    } catch (StringIndexOutOfBoundsException r) {
      System.out.println("clearedString: " + clearedString);
    }
    return clearedString;
  }

  private WordDefinitionData getPreBuildWordDefinition() {
    return WordDefinitionData.builder()
        .active(0)
        .user_id(4)
        .dictionary_id(5)
        .created_at(LocalDateTime.now().format(dateTimeFormatter))
        .build();
  }

  private HashMap<Integer, String> getSplittersMap() {

    HashMap<Integer, String> splitters = new HashMap<>();
    splitters.put(0, "vb.");
    splitters.put(1, "adj.");
    splitters.put(2, "adv.");
    splitters.put(3, "interj.");
    splitters.put(4, "pers.");
    splitters.put(5, "prep.");
    splitters.put(6, "subst.");
    splitters.put(7, "vb.");
    splitters.put(8, "[");

    return splitters;
  }

  private void writeListToJsonFile(ArrayList<WordDefinitionData> wordDefinitionData)
      throws JsonProcessingException {

    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = objectWriter.writeValueAsString(wordDefinitionData);

    Path newFile = Paths.get("src/main/resources/words.json");
    byte[] arr = json.getBytes();
    try {
      Files.write(newFile, arr);
    } catch (IOException ex) {
      System.out.print("Invalid Path");
    }
  }

}
