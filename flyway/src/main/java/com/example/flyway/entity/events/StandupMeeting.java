package com.example.flyway.entity.events;


import com.example.flyway.constants.enums.MeetingType;
import com.example.flyway.entity.ChatLine;
import com.example.flyway.entity.Plannable;
import com.example.flyway.entity.Project;
import com.example.flyway.entity.SpinningWheel;
import com.example.flyway.entity.Standup;
import com.example.flyway.entity.User;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "standup_event")
@Getter
@Setter
@NoArgsConstructor
public class StandupMeeting extends Meeting {

    @ManyToOne
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Standup standup;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standupMeeting", orphanRemoval = true)
    private Set<ChatLine> chatLines = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SpinningWheel spinningWheel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "standup_event_all_users",
        joinColumns = @JoinColumn(name = "standup_event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> allParticipants = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "standup_event_user",
        joinColumns = @JoinColumn(name = "standup_event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @Override
    public Plannable getTemplate() {
        return getStandup();
    }

    @Override
    public void setupChatline(ChatLine chatLine) {
        chatLine.setStandupMeeting(this);
    }

    @Override
    public MeetingType getType() {
        return MeetingType.STANDUP;
    }

    private boolean active = true;
}
