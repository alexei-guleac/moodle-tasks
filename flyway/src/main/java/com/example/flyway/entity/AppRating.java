package com.example.flyway.entity;

import com.example.flyway.constants.enums.AppFeedbackStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class AppRating extends BaseDao {

    @OneToOne
    private User user;

    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppFeedbackStatus status;
}
