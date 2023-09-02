package com.example.flyway.entity.retro.template.health_check;

import com.example.flyway.constants.enums.TeamHealthCheckCurrentState;
import com.example.flyway.constants.enums.TeamHealthCheckStateProgress;
import com.example.flyway.entity.BaseDao;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class TeamHealthCheckVote extends BaseDao {

    private String criteria;

    @Enumerated(value = EnumType.STRING)
    private TeamHealthCheckCurrentState currentState;

    @Enumerated(value = EnumType.STRING)
    private TeamHealthCheckStateProgress stateProgress;
}
