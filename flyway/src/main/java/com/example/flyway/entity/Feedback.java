package com.example.flyway.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Feedback extends BaseDao {

    @Column(length = 1000)
    private String comment;
    private Integer rating;
    @OneToOne
    private User user;
}
