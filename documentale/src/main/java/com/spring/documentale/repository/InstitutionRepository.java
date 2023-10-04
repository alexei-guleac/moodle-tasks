package com.spring.documentale.repository;

import com.spring.documentale.model.entity.Institution;
import com.spring.documentale.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

}
