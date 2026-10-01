package com.example.studentopportunitytracker.service;

import com.example.studentopportunitytracker.entity.Application;
import com.example.studentopportunitytracker.entity.Opportunity;
import com.example.studentopportunitytracker.entity.Reminder;
import com.example.studentopportunitytracker.entity.Student;
import com.example.studentopportunitytracker.exception.ResourceNotFoundException;
import com.example.studentopportunitytracker.repository.ApplicationRepository;
import com.example.studentopportunitytracker.repository.OpportunityRepository;
import com.example.studentopportunitytracker.repository.ReminderRepository;
import com.example.studentopportunitytracker.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private OpportunityRepository opportunityRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ReminderRepository reminderRepository;

    private StudentService studentService;
    private OpportunityService opportunityService;
    private ApplicationService applicationService;
    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, applicationRepository);
        opportunityService = new OpportunityService(opportunityRepository, applicationRepository);
        applicationService = new ApplicationService(applicationRepository, studentRepository, opportunityRepository);
        reminderService = new ReminderService(reminderRepository, applicationRepository);
    }

    @Test
    void testCreateApplication_Success() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");

        Opportunity opportunity = new Opportunity();
        opportunity.setId(2L);
        opportunity.setTitle("Test Opportunity");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(opportunityRepository.findById(2L)).thenReturn(Optional.of(opportunity));

        Application app = new Application();
        Student reqStudent = new Student();
        reqStudent.setId(1L);
        app.setStudent(reqStudent);

        Opportunity reqOpp = new Opportunity();
        reqOpp.setId(2L);
        app.setOpportunity(reqOpp);
        app.setStatus("INTERESTED");
        app.setAppliedDate(LocalDate.now());

        when(applicationRepository.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        Application created = applicationService.createApplication(app);

        assertNotNull(created);
        assertEquals(student, created.getStudent());
        assertEquals(opportunity, created.getOpportunity());
        verify(applicationRepository).save(app);
    }

    @Test
    void testCreateApplication_StudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Application app = new Application();
        Student reqStudent = new Student();
        reqStudent.setId(99L);
        app.setStudent(reqStudent);

        Opportunity reqOpp = new Opportunity();
        reqOpp.setId(1L);
        app.setOpportunity(reqOpp);

        assertThrows(ResourceNotFoundException.class, () -> applicationService.createApplication(app));
    }

    @Test
    void testDeleteOpportunity_BlockedWhenApplicationsExist() {
        when(opportunityRepository.existsById(1L)).thenReturn(true);
        when(applicationRepository.existsByOpportunityId(1L)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> opportunityService.deleteOpportunity(1L));

        assertTrue(ex.getReason().contains("applications have already been submitted"));
        verify(opportunityRepository, never()).deleteById(1L);
    }

    @Test
    void testDeleteOpportunity_SuccessWhenNoApplications() {
        when(opportunityRepository.existsById(1L)).thenReturn(true);
        when(applicationRepository.existsByOpportunityId(1L)).thenReturn(false);

        assertDoesNotThrow(() -> opportunityService.deleteOpportunity(1L));
        verify(opportunityRepository).deleteById(1L);
    }

    @Test
    void testDeleteStudent_NotFound() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(99L));
    }

    @Test
    void testDeleteStudent_SuccessWhenNoApplications() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(applicationRepository.existsByStudentId(1L)).thenReturn(false);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void testDeleteStudent_BlockedWhenApplicationsExist() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(applicationRepository.existsByStudentId(1L)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> studentService.deleteStudent(1L));

        assertTrue(ex.getReason().contains("applications have already been submitted"));
        verify(studentRepository, never()).deleteById(1L);
    }

    @Test
    void testUpdateApplication_Success() {
        Application existing = new Application();
        existing.setId(5L);
        existing.setStatus("APPLIED");

        when(applicationRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(applicationRepository.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        Application updateReq = new Application();
        updateReq.setStatus("INTERVIEW_SCHEDULED");
        updateReq.setNotes("First round scheduled next Monday");

        Application updated = applicationService.updateApplication(5L, updateReq);
        assertEquals("INTERVIEW_SCHEDULED", updated.getStatus());
        assertEquals("First round scheduled next Monday", updated.getNotes());
        verify(applicationRepository).save(existing);
    }

    @Test
    void testCreateReminder_Success() {
        Application app = new Application();
        app.setId(10L);

        when(applicationRepository.findById(10L)).thenReturn(Optional.of(app));

        Reminder reminder = new Reminder();
        Application reqApp = new Application();
        reqApp.setId(10L);
        reminder.setApplication(reqApp);
        reminder.setReminderDate(LocalDate.now());

        when(reminderRepository.save(any(Reminder.class))).thenAnswer(inv -> inv.getArgument(0));

        Reminder created = reminderService.createReminder(reminder);
        assertNotNull(created);
        assertEquals(app, created.getApplication());
        verify(reminderRepository).save(reminder);
    }

    @Test
    void testCreateReminder_ApplicationNotFound() {
        when(applicationRepository.findById(99L)).thenReturn(Optional.empty());

        Reminder reminder = new Reminder();
        Application reqApp = new Application();
        reqApp.setId(99L);
        reminder.setApplication(reqApp);
        reminder.setReminderDate(LocalDate.now());

        assertThrows(ResourceNotFoundException.class, () -> reminderService.createReminder(reminder));
    }

    @Test
    void testMarkReminderAsSent_Success() {
        Reminder reminder = new Reminder();
        reminder.setId(7L);
        reminder.setSent(false);

        when(reminderRepository.findById(7L)).thenReturn(Optional.of(reminder));
        when(reminderRepository.save(any(Reminder.class))).thenAnswer(inv -> inv.getArgument(0));

        Reminder sent = reminderService.markReminderAsSent(7L);
        assertTrue(sent.isSent());
        verify(reminderRepository).save(reminder);
    }
}
