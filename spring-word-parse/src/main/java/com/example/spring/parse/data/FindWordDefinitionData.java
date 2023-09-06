package com.example.spring.parse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindWordDefinitionData {

  private Integer id;

  private Integer dictionary_id;

  private String name;

  private String definition;

  private Integer active;

  private String created_at;

  private Set<String> synonyms = new HashSet<>();

  private Set<String> antonyms = new HashSet<>();

  private Set<String> relatives = new HashSet<>();

}
