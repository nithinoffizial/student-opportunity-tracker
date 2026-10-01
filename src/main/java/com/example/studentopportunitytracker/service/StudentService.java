package com.example.studentopportunitytracker.service;

import com.example.studentopportunitytracker.entity.Student;
import com.example.studentopportunitytracker.exception.ResourceNotFoundException;
import com.example.studentopportunitytracker.repository.ApplicationRepository;
import com.example.studentopportunitytracker.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;

    public StudentService(StudentRepository studentRepository, ApplicationRepository applicationRepository) {
        this.studentRepository = studentRepository;
        this.applicationRepository = applicationRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        if (applicationRepository.existsByStudentId(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot delete student: applications have already been submitted by this student. Deleting them would destroy application history.");
        }
        studentRepository.deleteById(id);
    }
}