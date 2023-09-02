package com.example.flyway.entity;

import com.example.flyway.entity.events.PlanningMeeting;
import com.example.flyway.entity.events.RetrospectiveMeeting;
import com.example.flyway.entity.events.StandupMeeting;
import com.example.flyway.entity.planning.Planning;
import com.example.flyway.entity.retro.Retrospective;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "participant",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User extends BaseDao {

    private String surname;
    private String forename;
    private String email;
    @Size(max = 120)
    private String password;

    @ManyToMany(mappedBy = "participants")
    private Set<Retrospective> retrospectives = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Planning> plannings = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<RetrospectiveMeeting> retrospectiveEvents = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<PlanningMeeting> planningEvents = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<StandupMeeting> standupMeetings = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "project_user",
        joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Role userRole;

    @ManyToMany
    @JoinTable(name = "project_roles_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "project_role_id"))
    private Set<ProjectRole> projectRoles = new HashSet<>();

    @PreRemove
    private void removeProjectList() {
        getProjects().clear();
    }

}
