package com.example.flyway.entity;

import com.example.flyway.entity.events.StandupMeeting;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SpinningWheel extends BaseDao {

    private Instant lastUpdatedAt;
    private Instant lastResetAt;

    @OneToOne(mappedBy = "spinningWheel", fetch = FetchType.LAZY)
    private StandupMeeting standupMeeting;

    @OneToOne
    private User userToSpeak;
    @ManyToMany
    @JoinTable(name = "spinning_wheel_participants",
        joinColumns = @JoinColumn(name = "spinning_wheel_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> allAvailableParticipants = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "spinning_wheel_standup_participants",
        joinColumns = @JoinColumn(name = "spinning_wheel_id"), inverseJoinColumns = @JoinColumn(name = "standup_participant_id"))
    private Set<StandupParticipant> allMeetingParticipants = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "spinning_wheel_participants_who_spoke",
        joinColumns = @JoinColumn(name = "spinning_wheel_id"), inverseJoinColumns = @JoinColumn(name = "standup_participant_id"))
    private Set<User> participantsWhoSpoke = new HashSet<>();
}
