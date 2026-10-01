package com.example.studentopportunitytracker.repository;

import com.example.studentopportunitytracker.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}