package com.example.flyway.entity.retro.template.event_ending;

import com.example.flyway.constants.enums.EventEndingVoteType;
import com.example.flyway.entity.BaseDao;
import com.example.flyway.entity.User;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class EventEndingVote extends BaseDao {

    private EventEndingVoteType voteType;
    @ManyToOne
    private User user;
}
