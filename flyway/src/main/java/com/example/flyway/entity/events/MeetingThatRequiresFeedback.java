package com.example.flyway.entity.events;

import com.example.flyway.entity.User;
import java.util.Set;

public abstract class MeetingThatRequiresFeedback extends Meeting {

    public abstract Set<User> getAllParticipantsWhoJoined();
}
