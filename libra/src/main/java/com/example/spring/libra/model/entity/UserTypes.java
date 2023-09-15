package com.example.spring.libra.model.entity;

import com.example.spring.libra.model.enums.UserType;
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
@Table(name = "users_types")
public class UserTypes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  private UserType userType;

  public UserTypes withId(Long id) {
    this.id = id;
    return this;
  }

  public UserTypes withUserType(UserType userType) {
    this.userType = userType;
    return this;
  }

  @Override
  public String toString() {
    return "" + userType;
  }
}





