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
@Table(name = "dm_projects")
public class Project {

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

  @Column(name = "name")
  private String name;

  @Column(name = "date_from")
  private LocalDateTime dateFrom;

  @Column(name = "date_till")
  private LocalDateTime dateTill;

  @Column(name = "additional_info")
  private String additionalInfo;

  @Column(name = "is_active")
  private Boolean isActive;

  public Project withId(Long id) {
    this.id = id;
    return this;
  }

  public Project withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String toString() {
    return name +
        ", " + userCreatedId +
        ", " + dateFrom;
  }
}





