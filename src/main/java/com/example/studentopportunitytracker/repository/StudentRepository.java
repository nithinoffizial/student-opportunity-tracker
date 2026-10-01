package com.example.studentopportunitytracker.repository;

import com.example.studentopportunitytracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}