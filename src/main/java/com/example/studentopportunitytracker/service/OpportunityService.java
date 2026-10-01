package com.example.studentopportunitytracker.service;

import com.example.studentopportunitytracker.entity.Opportunity;
import com.example.studentopportunitytracker.exception.ResourceNotFoundException;
import com.example.studentopportunitytracker.repository.ApplicationRepository;
import com.example.studentopportunitytracker.repository.OpportunityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;

    public OpportunityService(OpportunityRepository opportunityRepository,
                              ApplicationRepository applicationRepository) {
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
    }

    public Opportunity createOpportunity(Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }

    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    public Opportunity getOpportunityById(Long id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity not found with id: " + id));
    }

    public void deleteOpportunity(Long id) {
        if (!opportunityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Opportunity not found with id: " + id);
        }
        if (applicationRepository.existsByOpportunityId(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot delete opportunity: applications have already been submitted for this opportunity. Deleting it would destroy application history.");
        }
        opportunityRepository.deleteById(id);
    }
}