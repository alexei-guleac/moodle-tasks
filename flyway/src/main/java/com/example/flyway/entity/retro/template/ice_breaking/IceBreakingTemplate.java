package com.example.flyway.entity.retro.template.ice_breaking;

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
public class IceBreakingTemplate extends RetroTemplate {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<IceBreakingVote> votes = new HashSet<>();

}
