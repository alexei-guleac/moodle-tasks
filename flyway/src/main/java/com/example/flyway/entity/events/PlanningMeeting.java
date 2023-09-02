package com.example.flyway.entity.events;

import com.example.flyway.constants.enums.MeetingType;
import com.example.flyway.entity.ChatLine;
import com.example.flyway.entity.Plannable;
import com.example.flyway.entity.Project;
import com.example.flyway.entity.User;
import com.example.flyway.entity.planning.Planning;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "planning_event")
@Getter
@Setter
@NoArgsConstructor
public class PlanningMeeting extends MeetingThatRequiresFeedback {

    private boolean active = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planningMeeting", orphanRemoval = true)
    private Set<ChatLine> chatLines = new HashSet<>();

    @ManyToOne
    private Planning planning;

    @ManyToOne
    private Project project;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "planning_event_user",
        joinColumns = @JoinColumn(name = "planning_event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "planning_event_users_that_joined",
        joinColumns = @JoinColumn(name = "retrospective_event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> allParticipantsWhoJoined = new HashSet<>();

    @Override
    public Plannable getTemplate() {
        return getPlanning();
    }

    @Override
    public void setupChatline(ChatLine chatLine) {
        chatLine.setPlanningMeeting(this);
    }

    @Override
    public MeetingType getType() {
        return MeetingType.PLANNING;
    }
}
