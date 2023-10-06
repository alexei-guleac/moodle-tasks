package com.spring.documentale.repository;

import com.spring.documentale.model.entity.Documents;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Documents, Long> {

  List<Documents> findByNameStartsWithIgnoreCase(@Param("name") String name);
}
