package com.example.flyway.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "project_role")
public class ProjectRole extends BaseDao {

    @ManyToMany(mappedBy = "projectRoles")
    private Set<User> participants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "project_roles",
        joinColumns = @JoinColumn(name = "project_role_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Role role;
}
