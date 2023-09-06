package com.example.spring.parse.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordDefinition {

  private Integer user_id;

  private Integer dictionary_id;

  private String name;

  private String predefinition;

  private String definition;

  private Integer active;

  private String created_at;

}
