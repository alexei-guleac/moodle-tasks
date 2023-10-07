package com.spring.documentale.repository;

import com.spring.documentale.model.entity.DocumentTypeIerarchy;
import com.spring.documentale.model.entity.Documents;
import com.spring.documentale.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeIerarchyRepository extends JpaRepository<DocumentTypeIerarchy, Long> {

  List<DocumentTypeIerarchy> findByIdMacro(@Param("id") Long id);
}
