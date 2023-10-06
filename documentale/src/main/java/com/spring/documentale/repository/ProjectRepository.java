package com.spring.documentale.repository;

import com.spring.documentale.model.entity.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  List<Project> findByNameStartsWithIgnoreCase(@Param("name") String name);
}
