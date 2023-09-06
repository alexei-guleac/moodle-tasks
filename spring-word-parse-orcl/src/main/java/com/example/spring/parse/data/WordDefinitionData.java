package com.example.spring.parse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordDefinitionData {

  private Integer user_id;

  private Integer dictionary_id;

  private String name;

  private String predefinition;

  private String definition;

  private Integer active;

  private String created_at;

  @JsonIgnore
  private Set<String> synonyms = new HashSet<>();

  @JsonIgnore
  private Set<String> antonyms = new HashSet<>();

  @JsonIgnore
  private Set<String> relatives = new HashSet<>();

}
