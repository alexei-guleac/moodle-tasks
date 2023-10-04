package com.spring.documentale.repository;

import com.spring.documentale.model.entity.User;
import com.spring.documentale.model.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

}
