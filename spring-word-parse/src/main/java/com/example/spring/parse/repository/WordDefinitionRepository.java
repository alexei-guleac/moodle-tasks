package com.example.spring.parse.repository;

import com.example.spring.parse.entity.WordDefinition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordDefinitionRepository extends JpaRepository<WordDefinition, Long> {

  @Query("SELECT wd FROM WordDefinition wd WHERE wd.name like CONCAT('%',:word,'%')")
  List<WordDefinition> findByNameContainingIgnoreCase(@Param("word") String word);

}
