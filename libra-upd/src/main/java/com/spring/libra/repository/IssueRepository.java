package com.spring.libra.repository;

import com.spring.libra.model.entity.Issue;
import com.spring.libra.model.entity.Pos;
import com.spring.libra.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

  List<Issue> findByAssignedId(@Param("assignedId") User assignedId);

  List<Issue> findByPosId(@Param("posId") Pos posId);

  List<Issue> findByDescriptionStartsWithIgnoreCase(@Param("description") String description);

  List<Issue> findByAssignedIdAndDescriptionStartsWithIgnoreCase(
      @Param("assignedId") User assignedId, @Param("description") String description);

  List<Issue> findByPosIdAndDescriptionStartsWithIgnoreCase(
      @Param("posId") Pos posId, @Param("description") String description);
}
