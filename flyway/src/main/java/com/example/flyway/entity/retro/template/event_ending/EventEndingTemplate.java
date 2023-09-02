package com.example.flyway.entity.retro.template.event_ending;

import com.example.flyway.entity.RetroTemplate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EventEndingTemplate extends RetroTemplate {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<EventEndingVote> votes = new HashSet<>();

}
