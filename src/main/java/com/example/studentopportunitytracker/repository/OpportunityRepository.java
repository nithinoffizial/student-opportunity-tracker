package com.example.studentopportunitytracker.repository;

import com.example.studentopportunitytracker.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
}