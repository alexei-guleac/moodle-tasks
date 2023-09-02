package com.example.flyway.entity.retro.template.health_check;

import static com.example.flyway.constants.TeamHealthCheckDefaultCriteria.COMMUNICATION;
import static com.example.flyway.constants.TeamHealthCheckDefaultCriteria.GOALS_ALIGNMENT;
import static com.example.flyway.constants.TeamHealthCheckDefaultCriteria.PRODUCT_QUALITY;
import static com.example.flyway.constants.TeamHealthCheckDefaultCriteria.QUALITY_OF_PROCESSES;
import static com.example.flyway.constants.TeamHealthCheckDefaultCriteria.SPEED_OF_WORK;

import com.example.flyway.entity.RetroTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class TeamHealthCheckTemplate extends RetroTemplate {

    private Boolean revealResults = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamHealthCheckTemplate", fetch = FetchType.EAGER)
    private Set<TeamHealthCheckVoteBundle> voteBundles = new HashSet<>();

    @ElementCollection
    private List<String> criteria = new ArrayList<>(Arrays
        .asList(SPEED_OF_WORK, GOALS_ALIGNMENT, PRODUCT_QUALITY, COMMUNICATION,
            QUALITY_OF_PROCESSES));

}
