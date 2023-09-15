package com.example.spring.libra.repository;

import com.example.spring.libra.model.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLogin(String username);

  List<User> findByEmailStartsWithIgnoreCase(@Param("email") String email);
}
