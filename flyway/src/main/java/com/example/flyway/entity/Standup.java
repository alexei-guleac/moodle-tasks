package com.example.flyway.entity;

import com.example.flyway.constants.enums.Occurrences;
import com.example.flyway.entity.events.StandupMeeting;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "standup")
@Getter
@Setter
@NoArgsConstructor
public class Standup extends Plannable {

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<Occurrences> occurrences = new HashSet<>();

    @ManyToOne
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standup")
    private Set<StandupParticipant> standupParticipants = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standup")
    private Set<StandupMeeting> standupMeetings = new HashSet<>();

    private LocalTime time;

    @Override
    public Set<User> getParticipants() {
        Set<StandupParticipant> participants = getStandupParticipants();
        return participants.stream().map(StandupParticipant::getUser).collect(Collectors.toSet());
    }

    public Set<User> getActiveParticipants() {
        Set<StandupParticipant> participants = getStandupParticipants();
        return participants.stream().filter(StandupParticipant::isActive)
            .map(StandupParticipant::getUser).collect(Collectors.toSet());
    }
}
