package com.example.flyway.entity.events;


import com.example.flyway.constants.enums.MeetingType;
import com.example.flyway.entity.ChatLine;
import com.example.flyway.entity.Plannable;
import com.example.flyway.entity.Project;
import com.example.flyway.entity.RetroTemplate;
import com.example.flyway.entity.User;
import com.example.flyway.entity.retro.Retrospective;
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
@Table(name = "retro_event")
@Getter
@Setter
@NoArgsConstructor
public class RetrospectiveMeeting extends MeetingThatRequiresFeedback {

    private boolean active = true;

    private Integer currentOrdinal;

    private boolean saveNotes = true;

    @ManyToOne
    private Retrospective retrospective;

    @ManyToOne
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "retrospectiveMeeting", orphanRemoval = true)
    private Set<ChatLine> chatLines = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "retrospectiveMeeting")
    private Set<RetroTemplate> retroTemplates = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "retrospective_event_user",
        joinColumns = @JoinColumn(name = "retrospective_event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "retrospective_event_users_that_joined",
        joinColumns = @JoinColumn(name = "retrospective_event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> allParticipantsWhoJoined = new HashSet<>();

    @Override
    public Plannable getTemplate() {
        return getRetrospective();
    }

    @Override
    public void setupChatline(ChatLine chatLine) {
        chatLine.setRetrospectiveMeeting(this);
    }

    @Override
    public MeetingType getType() {
        return MeetingType.RETROSPECTIVE;
    }
}
