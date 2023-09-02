package com.example.flyway.entity.events;


import com.example.flyway.constants.enums.MeetingType;
import com.example.flyway.entity.BaseDao;
import com.example.flyway.entity.ChatLine;
import com.example.flyway.entity.Plannable;
import com.example.flyway.entity.Project;
import com.example.flyway.entity.User;
import java.time.Instant;
import java.util.Set;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Meeting extends BaseDao {

    private String name;
    private Instant startDate;
    private Instant endDate;
    private boolean isAnonymous = false;

    private Instant timerStartedAt;
    private Instant breakStartedAt;
    private Integer breakDuration;
    private Instant breakStoppedAt;
    private boolean breakTimerActivated = false;
    private String lastTimerValue;
    private Integer accumulatedBreakValue;
    private Integer timerDuration;

    public abstract Set<ChatLine> getChatLines();

    public abstract Plannable getTemplate();

    public abstract void setupChatline(ChatLine chatLine);

    public abstract boolean isActive();

    public abstract Project getProject();

    public abstract Set<User> getParticipants();

    public abstract MeetingType getType();

}
