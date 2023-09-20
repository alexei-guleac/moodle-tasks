package com.spring.libra.model.entity;

import com.spring.libra.model.enums.ConnectionType;
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
@Table(name = "connection_types")
public class ConnectionTypes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "connection_type")
  private ConnectionType connectionType;

  public ConnectionTypes withId(Long id) {
    this.id = id;
    return this;
  }

  public ConnectionTypes withConnectionType(ConnectionType connectionType) {
    this.connectionType = connectionType;
    return this;
  }
}





