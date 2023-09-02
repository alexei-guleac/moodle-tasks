package com.example.flyway.entity;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
public class BaseDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private Long createdBy;
    private Long updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || id == null
            || ((BaseDao) o).getId() == null) {
            return false;
        }
        BaseDao baseDao = (BaseDao) o;
        return id.equals(baseDao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
