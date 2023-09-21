package com.spring.libra.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "notifications")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "issue_id")
  private Issue issueId;

  @Column(name = "priority")
  private String priority;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_created_id")
  private User userCreatedId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "assigned_id")
  private User assignedId;

  @Column(name = "description")
  private String description;

  @Column(name = "assigned_date")
  private LocalDateTime assignedDate;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

}





