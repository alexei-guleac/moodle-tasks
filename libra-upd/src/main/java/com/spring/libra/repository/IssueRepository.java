package com.spring.libra.repository;

import com.spring.libra.model.entity.Issue;
import com.spring.libra.model.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

  List<Issue> findByDescriptionStartsWithIgnoreCase(@Param("description") String description);
}
