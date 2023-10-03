package com.spring.documentale.repository;

import com.spring.documentale.model.entity.UserRole;
import com.spring.documentale.model.enums.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRole, Long> {

  Optional<UserRole> findByRole(Role role);

}
