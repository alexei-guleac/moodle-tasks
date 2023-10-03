package com.spring.documentale.model.entity;

import com.spring.documentale.model.enums.Role;
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
@Table(name = "dm_user_roles")
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role")
  private Role role;

  public UserRole withId(Long id) {
    this.id = id;
    return this;
  }

  public UserRole withRole(Role role) {
    this.role = role;
    return this;
  }

  @Override
  public String toString() {
    return "" + role;
  }
}





