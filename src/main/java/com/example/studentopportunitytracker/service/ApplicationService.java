package com.example.studentopportunitytracker.service;

import com.example.studentopportunitytracker.entity.Application;
import com.example.studentopportunitytracker.entity.Opportunity;
import com.example.studentopportunitytracker.entity.Student;
import com.example.studentopportunitytracker.exception.ResourceNotFoundException;
import com.example.studentopportunitytracker.repository.ApplicationRepository;
import com.example.studentopportunitytracker.repository.OpportunityRepository;
import com.example.studentopportunitytracker.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final OpportunityRepository opportunityRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              StudentRepository studentRepository,
                              OpportunityRepository opportunityRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.opportunityRepository = opportunityRepository;
    }

    public Application createApplication(Application application) {
        if (application.getStudent() == null || application.getStudent().getId() == null) {
            throw new IllegalArgumentException("Student ID is required");
        }
        if (application.getOpportunity() == null || application.getOpportunity().getId() == null) {
            throw new IllegalArgumentException("Opportunity ID is required");
        }

        Student student = studentRepository.findById(application.getStudent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + application.getStudent().getId()));

        Opportunity opportunity = opportunityRepository.findById(application.getOpportunity().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + application.getOpportunity().getId()));

        application.setStudent(student);
        application.setOpportunity(opportunity);

        return applicationRepository.save(application);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
    }

    public Application updateApplication(Long id, Application updated) {
        Application existing = getApplicationById(id);
        if (updated.getStatus() != null && !updated.getStatus().isBlank()) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getAppliedDate() != null) {
            existing.setAppliedDate(updated.getAppliedDate());
        }
        if (updated.getNotes() != null) {
            existing.setNotes(updated.getNotes());
        }
        return applicationRepository.save(existing);
    }

    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }
        applicationRepository.deleteById(id);
    }
}