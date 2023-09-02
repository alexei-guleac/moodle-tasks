package com.example.flyway.entity.retro.template.agile;

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
public class AgileRetroTemplate extends RetroTemplate {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AgileRetroLine> lines = new HashSet<>();

}
