package com.example.spring.parse.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "word_definition")
public class WordDefinition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "user_id")
  private Integer user_id;

  @Column(name = "dictionary_id")
  private Integer dictionary_id;

  @Column(name = "name")
  private String name;

  @Column(name = "predefinition")
  private String predefinition;

  @ElementCollection
  @Column(columnDefinition="TEXT", length = 2048)
  @CollectionTable(name="definition")
  private List<String> definition;

  @Column(name = "active")
  private Integer active;

  @Column(name = "created_at")
  private LocalDateTime created_at;

  @Column(name = "synonyms")
  private String synonyms;

  @Column(name = "antonyms")
  private String antonyms;

  @Column(name = "relatives")
  private String relatives;

}
