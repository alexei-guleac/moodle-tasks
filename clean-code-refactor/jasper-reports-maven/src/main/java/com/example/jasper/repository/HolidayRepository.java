package com.example.jasper.repository;

import com.example.jasper.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {



}
