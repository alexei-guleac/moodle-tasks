package com.example.spring.libra.model.entity;

import com.example.spring.libra.model.enums.IssueLevel;
import com.example.spring.libra.model.enums.IssueType;
import java.time.LocalDateTime;
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
@Table(name = "issue_types")
public class IssueTypes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "issue_level")
  private IssueLevel issueLevel;

  @Enumerated(EnumType.STRING)
  @Column(name = "issue_type_name")
  private IssueType issueTypeName;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "parent_issue_id")
//  private Issue parentIssueId;

  @Column(name = "insert_date")
  private LocalDateTime insertDate;

  public IssueTypes withId(Long id) {
    this.id = id;
    return this;
  }

  public IssueTypes withIssueTypeName(IssueType issueTypeName) {
    this.issueTypeName = issueTypeName;
    return this;
  }

  @Override
  public String toString() {
    return "" + issueLevel +
        ", " + issueTypeName;
  }
}





