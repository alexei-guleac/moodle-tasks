package com.example.flyway.entity.retro.template.health_check;

import com.example.flyway.entity.BaseDao;
import com.example.flyway.entity.User;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TeamHealthCheckVoteBundle extends BaseDao {

    @ManyToOne
    private User user;

    @ManyToOne
    private TeamHealthCheckTemplate teamHealthCheckTemplate;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<TeamHealthCheckVote> votes = new HashSet<>();
}
