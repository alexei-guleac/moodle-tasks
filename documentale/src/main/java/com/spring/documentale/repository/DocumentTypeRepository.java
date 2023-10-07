package com.spring.documentale.repository;

import com.spring.documentale.model.entity.DocumentTypes;
import com.spring.documentale.model.entity.Documents;
import com.spring.documentale.model.entity.User;
import com.spring.documentale.model.enums.DocumentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentTypes, Long> {

  Optional<DocumentTypes> findByName(@Param("name") DocumentType name);

}
