package com.spring.documentale.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "dm_documents")
public class Documents {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "institution_id")
  private Institution institutionId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_created_id")
  private User userCreatedId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "document_types_id")
  private DocumentTypes documentTypesId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "project_id")
  private Project projectId;

  @Column(name = "name")
  private String name;

  @Column(name = "saved_path")
  private String savedPath;

  @Column(name = "upload_date")
  private LocalDateTime uploadDate;

  @Column(name = "additional_info")
  private String additionalInfo;

  @Column(name = "grouping_date")
  private LocalDateTime groupingDate;
}





