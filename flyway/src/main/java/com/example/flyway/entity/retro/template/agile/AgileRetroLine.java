package com.example.flyway.entity.retro.template.agile;

import com.example.flyway.constants.enums.AgileRetroLineColumn;
import com.example.flyway.entity.BaseDao;
import com.example.flyway.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AgileRetroLine extends BaseDao {

    @Enumerated(EnumType.STRING)
    private AgileRetroLineColumn agileRetroLineColumn;

    @Column(length = 500)
    private String line;

    @ManyToOne
    private User user;
}
