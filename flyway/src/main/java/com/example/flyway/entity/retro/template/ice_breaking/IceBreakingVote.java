package com.example.flyway.entity.retro.template.ice_breaking;

import com.example.flyway.constants.enums.IceBreakingVoteType;
import com.example.flyway.entity.BaseDao;
import com.example.flyway.entity.User;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class IceBreakingVote extends BaseDao {

    @ManyToOne
    private User user;
    private IceBreakingVoteType iceBreakingVoteType;
}
