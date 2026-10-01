package com.example.studentopportunitytracker.repository;

import com.example.studentopportunitytracker.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByOpportunityId(Long opportunityId);

    boolean existsByStudentId(Long studentId);

    List<Application> findByOpportunityId(Long opportunityId);

    List<Application> findByStudentId(Long studentId);
}