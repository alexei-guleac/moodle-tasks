package com.spring.libra.model.entity;

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
@Table(name = "users_details")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @Column(name = "login")
  private String login;

  @Column(name = "encoded_password")
  private String encodedPassword;

  @Column(name = "password")
  private String password;

  @Column(name = "telephone")
  private String telephone;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_type_id")
  private UserTypes userTypeId;

  public User withId(Long id) {
    this.id = id;
    return this;
  }

  @Override
  public String toString() {
    return "" + name + 
        ", " + email +
        ", " + userTypeId;
  }
}





