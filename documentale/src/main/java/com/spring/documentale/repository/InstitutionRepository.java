package com.spring.documentale.repository;

import com.spring.documentale.model.entity.Institution;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

  List<Institution> findByNameStartsWithIgnoreCase(@Param("name") String name);

}
