package com.example.flyway.entity;

import com.example.flyway.entity.retro.Retrospective;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
public class Project extends BaseDao {

    private String name;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<ProjectRole> projectRoles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Retrospective> retrospectives = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Standup> standups = new HashSet<>();
}