package com.spring.documentale.repository;

import com.spring.documentale.model.entity.Project;
import com.spring.documentale.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
