package com.example.flyway.entity;

import com.example.flyway.entity.events.PlanningMeeting;
import com.example.flyway.entity.events.RetrospectiveMeeting;
import com.example.flyway.entity.events.StandupMeeting;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_line")
@Getter
@Setter
@NoArgsConstructor
public class ChatLine extends BaseDao {

    @Column(length = 2400)
    private String lineText;

    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ChatLine repliedTo;

    @ManyToOne
    private StandupMeeting standupMeeting;

    @ManyToOne
    private RetrospectiveMeeting retrospectiveMeeting;

    @ManyToOne
    private PlanningMeeting planningMeeting;
}
