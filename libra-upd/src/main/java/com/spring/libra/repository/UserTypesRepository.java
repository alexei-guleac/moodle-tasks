package com.spring.libra.repository;

import com.spring.libra.model.entity.UserTypes;
import com.spring.libra.model.enums.UserType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypesRepository extends JpaRepository<UserTypes, Long> {

  Optional<UserTypes> findByUserType(UserType userType);

}
