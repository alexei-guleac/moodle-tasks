package com.example.flyway.entity;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandupParticipant extends BaseDao {

    public StandupParticipant(User user) {
        this.user = user;
    }

    @OneToOne
    private User user;

    private boolean active = true;

    private Boolean guest = false;

    @ManyToOne
    private Standup standup;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StandupParticipant that = (StandupParticipant) o;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
