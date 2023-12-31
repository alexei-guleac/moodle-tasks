package com.spring.libra.repository;

import com.spring.libra.model.entity.Notification;
import com.spring.libra.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByAssignedId(@Param("assignedId") User assignedId);

  List<Notification> findByDescriptionStartsWithIgnoreCase(
      @Param("description") String description);

  List<Notification> findByAssignedIdAndDescriptionStartsWithIgnoreCase(
      @Param("assignedId") User assignedId, @Param("description") String description);

  @Transactional
  @Modifying
  @Query("DELETE FROM Notification n WHERE n.issueId.id=:issueId")
  int deleteNotificationsByIssueId(@Param("issueId") Long issueId);
}
