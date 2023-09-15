package com.example.spring.libra.repository;

import com.example.spring.libra.model.entity.IssueTypes;
import com.example.spring.libra.model.entity.UserTypes;
import com.example.spring.libra.model.enums.IssueType;
import com.example.spring.libra.model.enums.UserType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueTypesRepository extends JpaRepository<IssueTypes, Long> {

  Optional<IssueTypes> findByIssueTypeName(IssueType issueTypeName);

}
