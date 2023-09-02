package com.example.flyway.entity;

import com.example.flyway.constants.enums.RetroTemplateType;
import com.example.flyway.entity.events.RetrospectiveMeeting;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RetroTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private Integer ordinal;
    @Enumerated(EnumType.STRING)
    private RetroTemplateType retroTemplateType;

    @ManyToOne(fetch = FetchType.LAZY)
    private RetrospectiveMeeting retrospectiveMeeting;

    @Transient
    private Integer currentOrdinal;
}
