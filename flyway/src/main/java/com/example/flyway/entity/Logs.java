package com.example.flyway.entity;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

@Entity
@Table(name = "access_logs",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")})
@Getter
@Setter
@NoArgsConstructor
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;
    private RequestMethod typeMethod;
    private String nameMethod;
    private String description;
    private Instant time = Instant.now();

    @Override
    public String toString() {
        return this.accountId + " " + typeMethod + " " + nameMethod + " " + description + " at "
            + time;
    }

}
