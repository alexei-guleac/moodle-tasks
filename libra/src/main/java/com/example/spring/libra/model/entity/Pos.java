package com.example.spring.libra.model.entity;

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
@Table(name = "pos")
public class Pos {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "pos_name")
  private String posName;

  @Column(name = "telephone")
  private String telephone;

  @Column(name = "cellphone")
  private String cellPhone;

  @Column(name = "address")
  private String address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id")
  private City cityId;

  @Column(name = "model")
  private String model;

  @Column(name = "brand")
  private String brand;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "connection_type_id")
  private ConnectionTypes connectionTypeId;

  @Column(name = "morning_opening")
  private Boolean morningOpening;

  @Column(name = "morning_closing")
  private Boolean morningClosing;

  @Column(name = "afternoon_opening")
  private Boolean afternoonOpening;

  @Column(name = "afternoon_closing")
  private Boolean afternoonClosing;

  @Column(name = "days_closed")
  private Integer daysClosed;

  @Column(name = "insert_date")
  private LocalDateTime insertDate;

  public Pos withId(Long id) {
    this.id = id;
    return this;
  }

  @Override
  public String toString() {
    return "" + posName +
        ", " + model +
        ", " + brand;
  }
}





