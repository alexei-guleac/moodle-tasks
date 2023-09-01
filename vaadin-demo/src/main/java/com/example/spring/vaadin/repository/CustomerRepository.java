package com.example.spring.vaadin.repository;

import com.example.spring.vaadin.entity.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  List<Customer> findByEmailStartsWithIgnoreCase(@Param("email") String email);

}
