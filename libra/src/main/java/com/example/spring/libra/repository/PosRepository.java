package com.example.spring.libra.repository;

import com.example.spring.libra.model.entity.Pos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PosRepository extends JpaRepository<Pos, Long> {

  List<Pos> findByPosNameStartsWithIgnoreCase(@Param("posName") String posName);
}
