package com.example.flyway.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserMeetingCounter extends BaseDao {

    private int meetingsSinceLastRating = 0;

    private Long lastMeetingId;

    @OneToOne
    private User user;

    public void incrementMeetingsSinceLastRating() {
        this.meetingsSinceLastRating++;
    }
}
