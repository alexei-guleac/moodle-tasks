package com.spring.documentale.model.entity;

import com.spring.documentale.model.enums.DocumentType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dm_document_types")
public class DocumentTypes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "code")
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "name")
  private DocumentType name;

  @Column(name = "type_description")
  private String typeDescription;

  @Column(name = "is_macro")
  private Boolean isMacro;

  @Column(name = "is_date_grouped")
  private Boolean isDateGrouped;
}





