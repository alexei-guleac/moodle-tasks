package com.example.spring.libra.repository;

import com.example.spring.libra.model.entity.Statuses;
import com.example.spring.libra.model.entity.UserTypes;
import com.example.spring.libra.model.enums.Status;
import com.example.spring.libra.model.enums.UserType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusesRepository extends JpaRepository<Statuses, Long> {

  Optional<Statuses> findByStatus(Status status);

}
