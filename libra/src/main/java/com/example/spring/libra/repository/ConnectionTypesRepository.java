package com.example.spring.libra.repository;

import com.example.spring.libra.model.entity.ConnectionTypes;
import com.example.spring.libra.model.enums.ConnectionType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionTypesRepository extends JpaRepository<ConnectionTypes, Long> {

  Optional<ConnectionTypes> findByConnectionType(ConnectionType connectionType);

}
