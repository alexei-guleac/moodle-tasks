package com.example.flyway.entity.planning;

import com.example.flyway.entity.BaseDao;
import com.example.flyway.entity.User;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PokerParticipant extends BaseDao {

    @OneToOne
    private User user;

    private boolean voted = false;

    private String votedComplexity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PokerParticipant that = (PokerParticipant) o;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
