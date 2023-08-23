package com.example.spring.batch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMERS_INFO")
public class Customer {

  @Id
  @Column(name = "CUSTOMER_ID")
  private int customerId;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "GENDER")
  private String gender;

  @Column(name = "CONTACT")
  private String contact;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "DOB")
  private String dob;

  public Customer() {
  }

  public Customer(int customerId, String firstName, String lastName, String email, String gender,
      String contact, String country, String dob) {
    this.customerId = customerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.gender = gender;
    this.contact = contact;
    this.country = country;
    this.dob = dob;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int id) {
    this.customerId = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contactNo) {
    this.contact = contactNo;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  @Override
  public String toString() {
    return  +customerId +
        "," + firstName +
        "," + lastName +
        "," + email +
        "," + gender +
        "," + contact +
        "," + country +
        "," + dob;
  }
}
