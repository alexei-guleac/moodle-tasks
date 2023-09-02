package com.example.flyway.entity.planning;

import com.example.flyway.entity.Plannable;
import com.example.flyway.entity.Project;
import com.example.flyway.entity.User;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "planning")
@Getter
@Setter
@NoArgsConstructor
public class Planning extends Plannable {

    @ManyToMany
    @JoinTable(name = "planning_user",
        joinColumns = @JoinColumn(name = "planning_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @ManyToOne
    private Project project;

    private Boolean requestFeedback;
}
