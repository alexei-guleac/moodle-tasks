package com.example.flyway.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Plannable extends BaseDao {

    private String name;
    private LocalTime time;
    private LocalDate date;
    private Instant lastMeetingEndedAt;
    private String link;
    private boolean includeNotes = true;
    private boolean includeTimer = true;
    private boolean archived = false;
    private Integer timerDuration;
    @Column(name = "meetingIsAnonymous")
    private boolean isAnonymous = false;


    public abstract Set<User> getParticipants();
}
