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
@Table(name = "issues")
public class Issue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "pos_id")
  private Pos posId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "issue_type_id")
  private IssueTypes issueTypeId;

  @Column(name = "problem_id")
  private Integer problemId;

  @Column(name = "priority")
  private String priority;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status_id")
  private Statuses statusId;

  @Column(name = "memo")
  private String memo;

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

  @Column(name = "modif_date")
  private LocalDateTime modifDate;

  @Column(name = "solution")
  private String solution;

  @Override
  public String toString() {
    return "Issue{" +
        "id=" + id +
        ", posId=" + posId +
        ", issueTypeId=" + issueTypeId +
        ", problemId=" + problemId +
        ", priority='" + priority + '\'' +
        ", statusId=" + statusId +
        ", memo='" + memo + '\'' +
        ", userCreatedId=" + userCreatedId +
        ", assignedId=" + assignedId +
        ", description='" + description + '\'' +
        ", assignedDate=" + assignedDate +
        ", creationDate=" + creationDate +
        ", modifDate=" + modifDate +
        ", solution='" + solution + '\'' +
        '}';
  }
}





