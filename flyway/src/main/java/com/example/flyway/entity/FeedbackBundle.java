package com.example.flyway.entity;

import com.example.flyway.constants.enums.MeetingType;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FeedbackBundle extends BaseDao {

    private boolean allUsersSubmittedFeedback = false;

    private Instant meetingEndedAt;

    private Instant meetingStartedAt;

    private Long meetingId;

    private String meetingName;

    @Enumerated(EnumType.STRING)
    private MeetingType type;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks = new HashSet<>();

    @ManyToOne
    private Project project;

    @ManyToMany
    @JoinTable(name = "feedback_bundle_users_seen",
        joinColumns = @JoinColumn(name = "feedback_bundle_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersThatSawTheFeedback = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "feedback_bundle_users",
        joinColumns = @JoinColumn(name = "feedback_bundle_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersThatShouldReceiveFeedback = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "feedback_bundle_users_that_should_vote",
        joinColumns = @JoinColumn(name = "feedback_bundle_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersThatShouldVote = new HashSet<>();

}
