package com.example.flyway.entity.planning;

import com.example.flyway.entity.BaseDao;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlanningPoker extends BaseDao {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "planning_poker_participant",
        joinColumns = @JoinColumn(name = "planning_poker_id"), inverseJoinColumns = @JoinColumn(name = "participant_id"))
    private Set<PokerParticipant> participants = new HashSet<>();

    private boolean cardsRevealed = false;

    @OneToOne
    private Planning planning;

    public void hideParticipantsVotes() {
        for (PokerParticipant participant : this.getParticipants()) {
            participant.setVotedComplexity(null);
        }
    }
}
