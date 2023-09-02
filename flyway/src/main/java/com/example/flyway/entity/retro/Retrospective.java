package com.example.flyway.entity.retro;

import com.example.flyway.entity.Plannable;
import com.example.flyway.entity.Project;
import com.example.flyway.entity.RetroTemplate;
import com.example.flyway.entity.User;
import com.example.flyway.entity.events.RetrospectiveMeeting;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
@Table(name = "retro")
@Getter
@Setter
@NoArgsConstructor
public class Retrospective extends Plannable {

    @ManyToMany
    @JoinTable(name = "retro_user",
        joinColumns = @JoinColumn(name = "retro_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<RetroTemplate> templates = new HashSet<>();

    @ManyToOne
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "retrospective")
    private Set<RetrospectiveMeeting> retrospectiveMeetings = new HashSet<>();

    private boolean requestFeedback;

    private boolean discussActionPoints;

    private boolean isTemplate;
}
